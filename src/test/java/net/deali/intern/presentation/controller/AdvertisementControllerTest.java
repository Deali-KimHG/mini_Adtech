package net.deali.intern.presentation.controller;

import net.deali.intern.domain.AdvertiseLog;
import net.deali.intern.domain.Advertisement;
import net.deali.intern.infrastructure.repository.AdvertiseLogRepository;
import net.deali.intern.infrastructure.repository.AdvertisementRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdvertisementControllerTest {
    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @BeforeAll
    static void setData(@Autowired AdvertisementRepository advertisementRepository,
                        @Autowired DataSource dataSource) throws SQLException {
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
        advertisementRepository.saveAll(advertisementList);

        Connection conn = dataSource.getConnection();
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("sql/schema.sql"));
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("sql/advertisement/data.sql"));
    }

    @AfterAll
    static void clean(@Autowired AdvertisementRepository advertisementRepository,
                      @Autowired DataSource dataSource) throws SQLException {
        Connection conn = dataSource.getConnection();
        // DROP Table occurs error in h2:1.4.200 -> downgrade to 1.4.199
        // Or change the version of hibernate to greater than 5.4.13.FINAL
        // Error: Cannot drop "CREATIVE" because "FKS5DQ7803F72WM181B2AGLPS3B" depends on it;
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("sql/cleanup.sql"));

        advertisementRepository.deleteAll();
    }

    @Test
    @DisplayName("노출 광고 10개 선정")
    public void select10advert(@Autowired AdvertiseLogRepository advertiseLogRepository) throws Exception {
        mvc.perform(
                get("/dsp/v1/advertisement/")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("테스트데이터05"))
                .andExpect(jsonPath("$[1].title").value("테스트데이터04"));

        mvc.perform(
                get("/core/v1/creative/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creativeCount.count").value(1));

        AdvertiseLog advertiseLog = advertiseLogRepository.findByCreativeId(5L);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm");
        assertThat(advertiseLog.getAdvertiseDate().format(dateTimeFormatter))
                .isEqualTo(LocalDateTime.now().format(dateTimeFormatter));
        assertThat(advertiseLog.getScore()).isEqualTo(0.6666666666666667);
    }
}