package net.deali.intern.presentation.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CreativeControllerTest {
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
    static void setData(@Autowired DataSource dataSource) throws Exception {
        try(Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("Creative/data.sql"));
        }
        // API를 호출해서 테스트데이터를 넣을 수 있지만, 권장하지 않음
        // POST API가 성공한다는 가정하에 테스트가 진행되므로 POST API에 의존하게됨.
        MockMultipartFile images = new MockMultipartFile("images", "first.txt", "multipart/form-data", "first".getBytes());

        Path directory = Paths.get(System.getProperty("user.dir") + "/images/1/").toAbsolutePath().normalize();
        Files.createDirectories(directory);

        String filename = StringUtils.cleanPath(images.getOriginalFilename());
        Path targetPath = directory.resolve(filename).normalize();

        images.transferTo(targetPath);


        MockMultipartFile images1 = new MockMultipartFile("images", "second.txt", "multipart/form-data", "second".getBytes());

        Path directory1 = Paths.get(System.getProperty("user.dir") + "/images/2/").toAbsolutePath().normalize();
        Files.createDirectories(directory1);

        String filename1 = StringUtils.cleanPath(images1.getOriginalFilename());
        Path targetPath1 = directory1.resolve(filename1).normalize();

        images1.transferTo(targetPath1);
    }

    @Test
    @DisplayName("소재 생성")
    public void createCreative() throws Exception {
        MockMultipartFile images = new MockMultipartFile("images", "test.txt", "multipart/form-data", "create test".getBytes());

        mvc.perform(
                multipart("/core/v1/creative/")
                        .file(images)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "생성 테스트")
                        .param("price", "3")
                        .param("advertiseStartDate", "2021-06-24T16:00")
                        .param("advertiseEndDate", "2021-06-25T16:00"))
        .andExpect(status().isCreated());

        mvc.perform(
                get("/core/v1/creative/3")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("생성 테스트"))
                .andExpect(jsonPath("$.price").value(3));
    }

    @Test
    @DisplayName("소재 수정 (이미지 수정 포함)")
    public void updateCreative() throws Exception {
        MockMultipartFile images = new MockMultipartFile("images", "update.txt", "multipart/form-data", "update".getBytes());

        mvc.perform(
                multipart("/core/v1/creative/2")
                .file(images)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .param("title", "업데이트 테스트")
                .param("price", "4")
                .param("advertiseStartDate", "2021-06-16T17:00")
                .param("advertiseEndDate", "2021-06-26T17:00")
        )
                .andExpect(status().isOk());

        mvc.perform(
                get("/core/v1/creative/2")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("업데이트 테스트"))
                .andExpect(jsonPath("$.price").value(4));
    }

    @Test
    @DisplayName("소재 삭제")
    public void deleteCreative() throws Exception {
        mvc.perform(
                delete("/core/v1/creative/2")
        )
                .andExpect(status().isOk());

        // Transactional이 없을 경우에는 DELETE 연산이 메모리 DB에는 적용되지 않고 영속성 컨텍스트에만 적용된 상태.
        // 그 상태에서 메모리 DB를 조회하게 되니까 DELETED가 적용되지 않은 상태
        // @Transactional을 활용하는 경우에 대해 생각
        // Service단에 트랜잭셔널 어노테이션을 넣는 이유는 오류가발생했을때 해당 로직의 내용을 롤백하기위해
        mvc.perform(
                get("/core/v1/creative/2")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELETED"));
    }

    @Test
    @DisplayName("소재 1개만 조회")
    public void findCreative() throws Exception {
        mvc.perform(
                get("/core/v1/creative/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트데이터1"))
                .andExpect(jsonPath("$.price").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("모든 소재 조회")
    public void findAllCreative() throws Exception {
        mvc.perform(
                get("/core/v1/creative/")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("테스트데이터1"))
                .andExpect(jsonPath("$[1].title").value("테스트데이터2"));
    }
}