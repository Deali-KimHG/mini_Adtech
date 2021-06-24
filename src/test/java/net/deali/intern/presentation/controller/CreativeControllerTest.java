package net.deali.intern.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CreativeControllerTest {
    @Autowired
    MockMvc mvc;


    @Autowired
    private WebApplicationContext ctx;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("소재 생성")
    public void createCreative() throws Exception {
        MockMultipartFile images = new MockMultipartFile("images", "file.txt", "multipart/form-data", "second contents".getBytes());

        mvc.perform(
                multipart("/core/v1/")
                        .file(images)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "test1")
                        .param("price", "3")
                        .param("exposureStartDate", "2021-06-24T16:11")
                        .param("exposureEndDate", "2021-06-24T16:12"))
        .andExpect(status().isOk())
        .andDo(print());
    }
}