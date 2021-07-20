package net.deali.intern.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.deali.intern.domain.Advertisement;
import net.deali.intern.domain.Creative;
import net.deali.intern.domain.CreativeStatus;
import net.deali.intern.infrastructure.exception.EntityControlException;
import net.deali.intern.infrastructure.exception.ErrorCode;
import net.deali.intern.infrastructure.repository.CreativeRepository;
import net.deali.intern.infrastructure.util.NowDateTimeJobParameter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class UpdateAdPoolJobConfiguration {
    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CreativeRepository creativeRepository;
    private final MongoTemplate mongoTemplate;
    private final NowDateTimeJobParameter jobParameter;

    private static final int chunkSize = 1000;

    @Bean
    @JobScope
    public NowDateTimeJobParameter jobParameter() {
        return new NowDateTimeJobParameter();
    }

    @Bean
    public Job updateAdvertisementPool() {
        return jobBuilderFactory.get("updateAdvertisementPool")
//                .incrementer(new RunIdIncrementer())
                .start(creativePagingStep())
                .next(advertisementPagingStep())
                .next(pausePagingStep())
                .build();
    }

    @Bean
    @JobScope
    public Step creativePagingStep() {
        return stepBuilderFactory.get("creativePagingStep")
                .<Creative, Advertisement>chunk(chunkSize)
                .reader(creativePagingReader())
                .processor(creativeProcessor())
                .writer(creativeWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step advertisementPagingStep() {
        return stepBuilderFactory.get("advertisementPagingStep")
                .<Advertisement, Advertisement>chunk(chunkSize)
                .reader(advertisementReader())
                .processor(advertisementProcessor())
                .writer(advertisementWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step pausePagingStep() {
        return stepBuilderFactory.get("pausePagingStep")
                .<Creative, Creative>chunk(chunkSize)
                .reader(pausePagingReader())
                .processor(pauseProcessor())
                .writer(pauseWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Creative> creativePagingReader() {

        JpaPagingItemReader<Creative> reader = new JpaPagingItemReader<Creative>() {
            @Override
            public int getPage() {
                return 0;
            }
        };
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("date1", jobParameter.getNowDate());
        parameters.put("date2", jobParameter.getNowDate());
        parameters.put("status1", CreativeStatus.DELETED);
        parameters.put("status2", CreativeStatus.PAUSE);

        reader.setQueryString("SELECT c FROM Creative c " +
                "WHERE c.advertiseStartDate <= :date1 AND c.advertiseEndDate > :date2 " +
                "AND c.status IS NOT :status1 AND c.status IS NOT :status2 ORDER BY id");
        reader.setParameterValues(parameters);
        reader.setPageSize(chunkSize);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setName("creativePagingReader");

        return reader;
    }

    @Bean
    public ItemProcessor<Creative, Advertisement> creativeProcessor() {
        return creative -> {
            creative.startAdvertise();
            creativeRepository.save(creative);
            return creative.toAdvertisement();
        };
    }

    @Bean
    public ItemWriter<Advertisement> creativeWriter() {
        return list -> {
            for(Advertisement advertisement : list) {
                Query query = new Query(Criteria.where("creativeId").is(advertisement.getCreativeId()));
                Update update = new Update()
                        .set("title", advertisement.getTitle())
                        .set("image", advertisement.getImage())
                        .set("price", advertisement.getPrice())
                        .set("creativeId", advertisement.getCreativeId())
                        .set("advertiseStartDate", advertisement.getAdvertiseStartDate())
                        .set("advertiseEndDate", advertisement.getAdvertiseEndDate())
                        .set("createdDate", advertisement.getCreatedDate())
                        .set("updatedDate", advertisement.getUpdatedDate());
                mongoTemplate.upsert(query, update, Advertisement.class);
            }
        };
    }

    @Bean
    @StepScope
    public MongoItemReader<Advertisement> advertisementReader() {
        return new MongoItemReaderBuilder<Advertisement>()
                .name("advertisementReader")
                .targetType(Advertisement.class)
                .query(new Query(new Criteria().orOperator(
                        Criteria.where("advertiseEndDate").lte(jobParameter.getNowDate()),
                        Criteria.where("advertiseStartDate").gt(jobParameter.getNowDate())))
                )
                .collection("advertisement")
                .sorts(Collections.singletonMap("creativeId", Sort.Direction.ASC))
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public ItemProcessor<Advertisement, Advertisement> advertisementProcessor() {
        return advertisement -> {
            Creative creative = creativeRepository.findById(advertisement.getCreativeId())
                    .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
            creative.stopAdvertise();
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

    @Bean
    @StepScope
    public JpaPagingItemReader<Creative> pausePagingReader() {

        JpaPagingItemReader<Creative> reader = new JpaPagingItemReader<Creative>() {
            @Override
            public int getPage() {
                return 0;
            }
        };
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("date", jobParameter.getNowDate());
        parameters.put("status", CreativeStatus.PAUSE);

        reader.setQueryString("SELECT c FROM Creative c " +
                "WHERE c.advertiseEndDate <= :date " +
                "AND c.status IS :status ORDER BY id");
        reader.setParameterValues(parameters);
        reader.setPageSize(chunkSize);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setName("pausePagingReader");

        return reader;
    }

    @Bean
    public ItemProcessor<Creative, Creative> pauseProcessor() {
        return creative -> {
            creative.stopAdvertise();
            return creative;
        };
    }

    @Bean
    public JpaItemWriter<Creative> pauseWriter() {
        JpaItemWriter<Creative> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
