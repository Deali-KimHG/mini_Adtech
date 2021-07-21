package net.deali.intern.infrastructure.configuration;

import net.deali.intern.domain.Advertisement;
import net.deali.intern.infrastructure.exception.EntityControlException;
import net.deali.intern.infrastructure.exception.ErrorCode;
import net.deali.intern.infrastructure.repository.AdvertisementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(args = {"--job.name=updateAdvertisementPool"})
class UpdateAdPoolJobTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setData() throws Exception {
        Connection conn = dataSource.getConnection();
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("sql/schema.sql"));
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("sql/adpool/data.sql"));

        List<Advertisement> advertisementList = new ArrayList<>();
        advertisementList.add(
                new Advertisement("테스트데이터01", "image01.txt", 1L, 1L,
                        LocalDateTime.of(2021, 6, 25, 12, 0),
                        LocalDateTime.of(2021, 7, 1, 12, 0),
                        LocalDateTime.of(2021, 6, 25, 12, 0),
                        LocalDateTime.of(2021, 6, 27, 12, 0)));
        advertisementList.add(
                new Advertisement("테스트데이터02", "image02.txt", 2L, 2L,
                        LocalDateTime.of(2021, 6, 26, 12, 0),
                        LocalDateTime.of(2021, 7, 2, 12, 0),
                        LocalDateTime.of(2021, 6, 26, 12, 0),
                        LocalDateTime.of(2021, 6, 28, 12, 0)));
        advertisementList.add(
                new Advertisement("테스트데이터03", "image03.txt", 3L, 3L,
                        LocalDateTime.of(2021, 6, 27, 12, 0),
                        LocalDateTime.of(2021, 7, 3, 12, 0),
                        LocalDateTime.of(2021, 6, 27, 12, 0),
                        LocalDateTime.of(2021, 6, 29, 12, 0)));
        advertisementList.add(
                new Advertisement("테스트데이터04", "image04.txt", 4L, 4L,
                        LocalDateTime.of(2021, 6, 28, 12, 0),
                        LocalDateTime.of(2021, 7, 4, 12, 0),
                        LocalDateTime.of(2021, 6, 28, 12, 0),
                        LocalDateTime.of(2021, 6, 30, 12, 0)));
        advertisementList.add(
                new Advertisement("테스트데이터05", "image05.txt", 5L, 5L,
                        LocalDateTime.of(2021, 6, 29, 12, 0),
                        LocalDateTime.of(2021, 7, 5, 12, 0),
                        LocalDateTime.of(2021, 6, 29, 12, 0),
                        LocalDateTime.of(2021, 7, 1, 12, 0)));
        advertisementList.add(
                new Advertisement("테스트데이터06", "image06.txt", 6L, 6L,
                        LocalDateTime.of(2021, 6, 29, 12, 0),
                        LocalDateTime.of(2021, 7, 5, 12, 0),
                        LocalDateTime.of(2021, 6, 29, 12, 0),
                        LocalDateTime.of(2021, 7, 1, 12, 0)));
        advertisementRepository.saveAll(advertisementList);
    }

    @AfterEach
    void cleanData() throws Exception {
        Connection conn = dataSource.getConnection();
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("sql/cleanup.sql"));

        advertisementRepository.deleteAll();
    }

    @Test
    @DisplayName("배치 테스트")
    public void batchTest(@Autowired AdvertisementRepository advertisementRepository) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("nowDate",
                        LocalDateTime.of(2021, 7, 4, 12, 0).format(
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                        ))
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertThatThrownBy(() -> advertisementRepository.findByCreativeId(1L)
        .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_ADVERTISEMENT_FAIL)))
                .isInstanceOf(EntityControlException.class)
                .hasMessage(ErrorCode.FIND_ADVERTISEMENT_FAIL.getMessage());

        Advertisement advertisement1 = advertisementRepository.findByCreativeId(6L)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_ADVERTISEMENT_FAIL));
        assertThat(advertisement1.getTitle()).isEqualTo("테스트데이터06");
        assertThat(advertisement1.getPrice()).isEqualTo(6L);
        assertThat(advertisement1.getCreativeId()).isEqualTo(6L);
        assertThat(advertisement1.getAdvertiseStartDate()).isEqualTo(
                LocalDateTime.of(2021, 6, 30, 12, 0)
        );
        assertThat(advertisement1.getAdvertiseEndDate()).isEqualTo(
                LocalDateTime.of(2021, 7, 6, 12, 0)
        );
        assertThat(advertisement1.getCreatedDate()).isEqualTo(
                LocalDateTime.of(2021, 6, 30, 12, 0)
        );
        assertThat(advertisement1.getUpdatedDate()).isEqualTo(
                LocalDateTime.of(2021, 7, 2, 12, 0)
        );

        Advertisement advertisement2 = advertisementRepository.findByCreativeId(10L)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_ADVERTISEMENT_FAIL));
        assertThat(advertisement2.getTitle()).isEqualTo("테스트데이터10");
        assertThat(advertisement2.getPrice()).isEqualTo(10L);
        assertThat(advertisement2.getCreativeId()).isEqualTo(10L);
        assertThat(advertisement2.getAdvertiseStartDate()).isEqualTo(
                LocalDateTime.of(2021, 7, 4, 12, 0)
        );
        assertThat(advertisement2.getAdvertiseEndDate()).isEqualTo(
                LocalDateTime.of(2021, 7, 10, 12, 0)
        );
        assertThat(advertisement2.getCreatedDate()).isEqualTo(
                LocalDateTime.of(2021, 7, 4, 12, 0)
        );
        assertThat(advertisement2.getUpdatedDate()).isEqualTo(
                LocalDateTime.of(2021, 7, 6, 12, 0)
        );

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
}