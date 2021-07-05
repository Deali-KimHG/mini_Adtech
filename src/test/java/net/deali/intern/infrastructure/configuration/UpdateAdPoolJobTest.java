package net.deali.intern.infrastructure.configuration;

import net.deali.intern.domain.Creative;
import net.deali.intern.domain.CreativeCount;
import net.deali.intern.infrastructure.repository.AdvertisementRepository;
import net.deali.intern.infrastructure.repository.CreativeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@SpringBootTest(classes = UpdateAdPoolJobConfiguration.class)
class UpdateAdPoolJobTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private CreativeRepository creativeRepository;

    @AfterEach
    public void tearDown() throws Exception {
        advertisementRepository.deleteAll();
        creativeRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("배치 테스트")
    public void batchTest() throws Exception {
        creativeRepository.save(Creative.builder()
                .title("테스트데이터01").price(1L)
                .advertiseStartDate(LocalDateTime.of(2021, 7, 1, 12, 0))
                .advertiseEndDate(LocalDateTime.of(2021, 7, 8, 12, 0))
                .creativeCount(CreativeCount.builder().count(0L).build())
                .build());
        creativeRepository.save(Creative.builder()
                .title("테스트데이터02").price(2L)
                .advertiseStartDate(LocalDateTime.of(2021, 7, 2, 12, 0))
                .advertiseEndDate(LocalDateTime.of(2021, 7, 9, 12, 0))
                .creativeCount(CreativeCount.builder().count(0L).build())
                .build());

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
}