package net.deali.intern.presentation.controller;

import net.deali.intern.domain.Advertisement;
import net.deali.intern.infrastructure.exception.EntityControlException;
import net.deali.intern.infrastructure.exception.ErrorCode;
import net.deali.intern.infrastructure.repository.AdvertisementRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdPoolControllerTest {
    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @BeforeAll
    static void setDate(@Autowired AdvertisementRepository advertisementRepository,
                 @Autowired DataSource dataSource) throws SQLException {

        List<Advertisement> advertisementList = new ArrayList<>();
        advertisementList.add(
                new Advertisement("테스트데이터01", "image01.txt", 1L, 1L,
                        LocalDateTime.of(2021, 6, 25, 12, 0),
                        LocalDateTime.of(2021, 7, 1, 12, 0),
                        LocalDateTime.of(2021, 6, 27, 12, 0)));
        advertisementList.add(
                new Advertisement("테스트데이터02", "image02.txt", 2L, 2L,
                        LocalDateTime.of(2021, 6, 26, 12, 0),
                        LocalDateTime.of(2021, 7, 2, 12, 0),
                        LocalDateTime.of(2021, 6, 28, 12, 0)));
        advertisementList.add(
                new Advertisement("테스트데이터03", "image03.txt", 3L, 3L,
                        LocalDateTime.of(2021, 6, 27, 12, 0),
                        LocalDateTime.of(2021, 7, 3, 12, 0),
                        LocalDateTime.of(2021, 6, 29, 12, 0)));
        advertisementList.add(
                new Advertisement("테스트데이터04", "image04.txt", 4L, 4L,
                        LocalDateTime.of(2021, 6, 28, 12, 0),
                        LocalDateTime.of(2021, 7, 4, 12, 0),
                        LocalDateTime.of(2021, 6, 30, 12, 0)));
        advertisementList.add(
                new Advertisement("테스트데이터05", "image05.txt", 5L, 5L,
                        LocalDateTime.of(2021, 6, 29, 12, 0),
                        LocalDateTime.of(2021, 7, 5, 12, 0),
                        LocalDateTime.of(2021, 7, 1, 12, 0)));
        advertisementList.add(
                new Advertisement("테스트데이터06", "image06.txt", 6L, 6L,
                        LocalDateTime.of(2021, 6, 30, 12, 0),
                        LocalDateTime.of(2021, 7, 6, 12, 0),
                        LocalDateTime.of(2021, 7, 2, 12, 0)));
        advertisementRepository.saveAll(advertisementList);

        Connection conn = dataSource.getConnection();
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("AdPool/data.sql"));
    }

    @Test
    @DisplayName("기간에 포함되는 광고 삽입")
    public void insertAdPool() throws Exception {
        mvc.perform(
                post("/dsp/v1/adpool/")
        )
                .andExpect(status().isCreated());

        Advertisement advertisement = advertisementRepository.findByCreativeId(7L)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_ADVERTISEMENT_FAIL));
        assertThat(advertisement.getTitle()).isEqualTo("테스트데이터07");
    }

    @Test
    @DisplayName("기간 벗어난 광고 삭제")
    public void deleteAdPool() throws Exception {
        mvc.perform(
                delete("/dsp/v1/adpool/")
        )
                .andExpect(status().isOk());

        assertThatThrownBy(() -> {
            advertisementRepository.findByCreativeId(1L)
            .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_ADVERTISEMENT_FAIL));
        })
                .isInstanceOf(EntityControlException.class)
                .hasMessage(ErrorCode.FIND_ADVERTISEMENT_FAIL.getMessage());
    }
}