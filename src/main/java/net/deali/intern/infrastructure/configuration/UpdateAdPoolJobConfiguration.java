package net.deali.intern.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.deali.intern.domain.Advertisement;
import net.deali.intern.domain.Creative;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class UpdateAdPoolJobConfiguration {
    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final int chunkSize = 1000;

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("updateAdvertisementPool")
                .start(creativePagingStep())
                .build();
    }

    @Bean
    @JobScope
    public Step creativePagingStep() {
        return stepBuilderFactory.get("creativePagingStep")
                .<Creative, Advertisement>chunk(chunkSize)
                .reader(creativePagingReader())
                .processor(creativePagingProcessor())
                .writer(writer())
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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm");
        parameters.put("date1", LocalDateTime.now().format(dateTimeFormatter));
        parameters.put("date2", LocalDateTime.now().format(dateTimeFormatter));

        reader.setQueryString("SELECT c FROM Creative c WHERE c.advertiseStartDate <= :date1 AND c.advertiseEndDate > :date2 ORDER BY id");
        reader.setParameterValues(parameters);
        reader.setPageSize(chunkSize);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setName("creativePagingReader");

        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<Creative, Advertisement> creativePagingProcessor() {
        return Creative::toAdvertisement;
    }

    @Bean
    @StepScope
    public JpaItemWriter<Advertisement> writer() {
        JpaItemWriter<Advertisement> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
