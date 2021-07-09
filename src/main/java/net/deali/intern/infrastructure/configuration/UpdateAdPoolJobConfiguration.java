package net.deali.intern.infrastructure.configuration;

import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import net.deali.intern.domain.Advertisement;
import net.deali.intern.domain.Creative;
import net.deali.intern.domain.CreativeStatus;
import org.bson.conversions.Bson;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.persistence.EntityManagerFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class UpdateAdPoolJobConfiguration {
    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MongoTemplate mongoTemplate;

    private static final int chunkSize = 1000;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Bean
    public Job updateAdvertisementPool() {
        return jobBuilderFactory.get("updateAdvertisementPool")
                .incrementer(new RunIdIncrementer())
                .start(creativePagingStep())
                .next(advertisementPagingStep())
                .build();
    }

    @Bean
    public Step creativePagingStep() {
        return stepBuilderFactory.get("creativePagingStep")
                .<Creative, Advertisement>chunk(chunkSize)
                .reader(creativePagingReader())
                .processor(creativeProcessor())
                .writer(creativeWriter())
                .build();
    }

    @Bean
    public Step advertisementPagingStep() {
        return stepBuilderFactory.get("advertisementPagingStep")
                .<Advertisement, Advertisement>chunk(chunkSize)
                .reader(advertisementReader())
                .processor(advertisementProcessor())
                .writer(advertisementWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Creative> creativePagingReader() {

        JpaPagingItemReader<Creative> reader = new JpaPagingItemReader<Creative>() {
            @Override
            public int getPage() {
                return 0;
            }
        };
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("date1", LocalDateTime.parse(LocalDateTime.now().format(dateTimeFormatter)));
        parameters.put("date2", LocalDateTime.parse(LocalDateTime.now().format(dateTimeFormatter)));
        parameters.put("status", CreativeStatus.DELETED);

        reader.setQueryString("SELECT c FROM Creative c " +
                "WHERE c.advertiseStartDate <= :date1 AND c.advertiseEndDate > :date2 " +
                "AND c.status IS NOT :status ORDER BY id");
        reader.setParameterValues(parameters);
        reader.setPageSize(chunkSize);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setName("creativePagingReader");

        return reader;
    }

    @Bean
    public ItemProcessor<Creative, Advertisement> creativeProcessor() {
        return Creative::toAdvertisement;
    }

    @Bean
    public ItemWriter<Advertisement> creativeWriter() {
        return list -> {
            for(Advertisement advertisement : list) {
                Query query = new Query(Criteria.where("creativeId").is(advertisement.getCreativeId()));
                Advertisement find = mongoTemplate.findOne(query, Advertisement.class);
                if(find == null) {
                    advertisement.updateDate();
                    mongoTemplate.insert(advertisement);
                } else {
                    Update update = new Update()
                            .set("title", advertisement.getTitle())
                            .set("image", advertisement.getImage())
                            .set("price", advertisement.getPrice())
                            .set("advertiseStartDate", advertisement.getAdvertiseStartDate().plusHours(9))
                            .set("advertiseEndDate", advertisement.getAdvertiseEndDate().plusHours(9))
                            .set("updatedDate", advertisement.getUpdatedDate());
                    mongoTemplate.upsert(query, update, Advertisement.class);
                }
            }
        };
    }

    @Bean
    public MongoItemReader<Advertisement> advertisementReader() {
        LocalDateTime date = LocalDateTime.parse(LocalDateTime.now().format(dateTimeFormatter));
        return new MongoItemReaderBuilder<Advertisement>()
                .name("advertisementReader")
                .targetType(Advertisement.class)
                .query(new Query(new Criteria().orOperator(
                        Criteria.where("advertiseEndDate").lte(date),
                        Criteria.where("advertiseStartDate").gt(date)))
                )
                .collection("advertisement")
                .sorts(Collections.singletonMap("creativeId", Sort.Direction.ASC))
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public ItemProcessor<Advertisement, Advertisement> advertisementProcessor() {
        return (advertisement) -> {
            System.out.println(advertisement.getCreativeId());
            return advertisement;
        };
    }

    @Bean
    public MongoItemWriter<Advertisement> advertisementWriter() {
        return new MongoItemWriterBuilder<Advertisement>()
                    .collection("advertisement")
                    .template(mongoTemplate)
                    .delete(true)
                    .build();
    }
}
