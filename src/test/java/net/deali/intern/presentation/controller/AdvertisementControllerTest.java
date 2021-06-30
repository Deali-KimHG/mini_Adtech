package net.deali.intern.presentation.controller;

import net.deali.intern.domain.Advertisement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdvertisementControllerTest {
    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @BeforeAll
    static void setData(@Autowired DataSource dataSource) throws SQLException {
        try(Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("data.sql"));
        }
    }

    @Test
    @DisplayName("노출 광고 10개 선정")
    public void expose10advert() {

    }

    @Test
    @DisplayName("광고 풀 삽입")
    public void insertAdPool() throws Exception {
        mvc.perform(
                post("/dsp/v1/")
        )
                .andExpect(status().isOk());

        List<Advertisement> advertisementList = mongoTemplate.findAll(Advertisement.class);
        assertThat(advertisementList.get(0).getCreativeId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("광고 풀 삭제")
    public void deleteAdPool() {

    }
}