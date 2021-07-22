package net.deali.intern.presentation.controller;

import net.deali.intern.domain.Advertisement;
import net.deali.intern.domain.Creative;
import net.deali.intern.domain.CreativeImage;
import net.deali.intern.infrastructure.exception.EntityControlException;
import net.deali.intern.infrastructure.exception.ErrorCode;
import net.deali.intern.infrastructure.repository.AdvertisementRepository;
import net.deali.intern.infrastructure.repository.CreativeRepository;
import org.junit.jupiter.api.*;
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
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    void setup(@Autowired AdvertisementRepository advertisementRepository) {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
        advertisementRepository.save(Advertisement.builder()
                .title("테스트데이터2")
                .price(2L)
                .image("테스트데이터02.txt")
                .creativeId(2L)
                .advertiseStartDate(LocalDateTime.of(2021, 7, 2, 17, 0))
                .advertiseEndDate(LocalDateTime.of(2021, 7, 12, 17, 0))
                .updatedDate(LocalDateTime.now())
                .build());
    }

    @AfterEach
    void cleanup(@Autowired AdvertisementRepository advertisementRepository) {
        advertisementRepository.deleteAll();
    }

    @BeforeAll
    static void setData(@Autowired CreativeRepository creativeRepository,
                        @Autowired DataSource dataSource) throws Exception {
        Connection conn = dataSource.getConnection();
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("sql/schema.sql"));
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("sql/creative/data.sql"));

        Path directory = Paths.get("/usr/local/var/www/images/").toAbsolutePath().normalize();

        // API를 호출해서 테스트데이터를 넣을 수 있지만, 권장하지 않음
        // POST API가 성공한다는 가정하에 테스트가 진행되므로 POST API에 의존하게됨.
        MockMultipartFile images1 = new MockMultipartFile("images", "테스트데이터01.jpg", "multipart/form-data", "테스트데이터01".getBytes());

        Creative creative1 = creativeRepository.findById(1L)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
        List<CreativeImage> creativeImage1 = creative1.getCreativeImages();

        String filename1 = StringUtils.cleanPath(creativeImage1.get(0).getId() + "." + creativeImage1.get(0).getExtension());
        Path targetPath1 = directory.resolve(filename1).normalize();

        images1.transferTo(targetPath1);

        MockMultipartFile images2 = new MockMultipartFile("images", "테스트데이터02.jpg", "multipart/form-data", "테스트데이터02".getBytes());

        Creative creative2 = creativeRepository.findById(2L)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
        List<CreativeImage> creativeImage2 = creative2.getCreativeImages();

        String filename2 = StringUtils.cleanPath(creativeImage2.get(0).getId() + "." + creativeImage2.get(0).getExtension());
        Path targetPath2 = directory.resolve(filename2).normalize();

        images2.transferTo(targetPath2);

        MockMultipartFile images3 = new MockMultipartFile("images", "테스트데이터03.jpg", "multipart/form-data", "테스트데이터03".getBytes());

        Creative creative3 = creativeRepository.findById(3L)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
        List<CreativeImage> creativeImage3 = creative3.getCreativeImages();

        String filename3 = StringUtils.cleanPath(creativeImage3.get(0).getId() + "." + creativeImage3.get(0).getExtension());
        Path targetPath3 = directory.resolve(filename3).normalize();

        images3.transferTo(targetPath3);

        MockMultipartFile images4 = new MockMultipartFile("images", "테스트데이터04.jpg", "multipart/form-data", "테스트데이터04".getBytes());

        Creative creative4 = creativeRepository.findById(4L)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
        List<CreativeImage> creativeImage4 = creative4.getCreativeImages();

        String filename4 = StringUtils.cleanPath(creativeImage4.get(0).getId() + "." + creativeImage4.get(0).getExtension());
        Path targetPath4 = directory.resolve(filename4).normalize();

        images4.transferTo(targetPath4);
    }

    @AfterAll
    static void cleanData(@Autowired AdvertisementRepository advertisementRepository,
                      @Autowired CreativeRepository creativeRepository,
                      @Autowired DataSource dataSource) throws SQLException {
        List<Creative> creativeList = creativeRepository.findAll();
        Path directory = Paths.get("/usr/local/var/www/images/").toAbsolutePath().normalize();

        for(Creative creative : creativeList) {
            List<CreativeImage> image = creative.getCreativeImages();
            File file = new File(directory + "/" +  image.get(0).getId().toString() + "." + image.get(0).getExtension());

            if(file.exists()) {
                file.delete();
            }
        }

        Connection conn = dataSource.getConnection();
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("sql/cleanup.sql"));

        advertisementRepository.deleteAll();
    }

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Test
    @DisplayName("소재 생성 성공 테스트")
    public void createSuccess(@Autowired CreativeRepository creativeRepository) throws Exception {
        MockMultipartFile images = new MockMultipartFile("images", "생성테스트.jpg", "multipart/form-data", "생성테스트".getBytes());

        mvc.perform(
                multipart("/core/v1/creative/")
                        .file(images)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "생성테스트")
                        .param("price", "5")
                        .param("advertiseStartDate", "2021-07-24T16:00")
                        .param("advertiseEndDate", "2021-07-25T16:00"))
        .andExpect(status().isCreated());

        mvc.perform(
                get("/core/v1/creative/7")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("생성테스트"))
                .andExpect(jsonPath("$.price").value(5))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.advertiseStartDate").value("2021-07-24T16:00"))
                .andExpect(jsonPath("$.advertiseEndDate").value("2021-07-25T16:00"));

        Creative creative = creativeRepository.findById(7L)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
        CreativeImage image = creative.getCreativeImages().get(0);
        File file = new File("/usr/local/var/www/images/" + image.getId() + "." + image.getExtension());
        if(file.exists())
            file.delete();
    }

    @Test
    @DisplayName("소재 생성 실패 테스트")
    public void createFail() throws Exception {
        mvc.perform(
                multipart("/core/v1/creative/")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Invalid input value"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("소재 생성 실패 테스트 (기간 오류)")
    public void createFailDate() throws Exception {
        MockMultipartFile images = new MockMultipartFile("images", "생성테스트.jpg", "multipart/form-data", "생성테스트".getBytes());
        mvc.perform(
                multipart("/core/v1/creative/")
                        .file(images)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "생성테스트")
                        .param("price", "5")
                        .param("advertiseStartDate", "2021-07-28T16:00")
                        .param("advertiseEndDate", "2021-07-25T16:00"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Invalid input date"))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    @DisplayName("소재 수정 성공 테스트 (이미지 수정 포함)")
    public void updateWithImageSuccess() throws Exception {
        MockMultipartFile images = new MockMultipartFile("images", "업데이트테스트.jpg", "multipart/form-data", "업데이트테스트".getBytes());

        mvc.perform(
                multipart("/core/v1/creative/1")
                .file(images)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .param("title", "업데이트 테스트")
                .param("price", "6")
                .param("advertiseStartDate", "2021-07-24T17:00")
                .param("advertiseEndDate", "2021-07-25T17:00")
        )
                .andExpect(status().isOk());

        mvc.perform(
                get("/core/v1/creative/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("업데이트 테스트"))
                .andExpect(jsonPath("$.price").value(6))
                .andExpect(jsonPath("$.advertiseStartDate").value("2021-07-24T17:00"))
                .andExpect(jsonPath("$.advertiseEndDate").value("2021-07-25T17:00"));
    }

    @Test
    @DisplayName("소재 수정 성공 테스트 (이미지 미포함)")
    public void updateWithoutImageSuccess() throws Exception {
        mvc.perform(
                multipart("/core/v1/creative/1")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "업데이트 테스트")
                        .param("price", "6")
        )
                .andExpect(status().isOk());

        mvc.perform(
                get("/core/v1/creative/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("업데이트 테스트"))
                .andExpect(jsonPath("$.price").value(6))
                .andExpect(jsonPath("$.advertiseStartDate").value("2021-07-24T17:00"))
                .andExpect(jsonPath("$.advertiseEndDate").value("2021-07-27T17:00"));
    }

    @Test
    @DisplayName("소재 수정 성공 테스트 (동일한 이미지 포함)")
    public void updateWithSameImageSuccess() throws Exception {
        MockMultipartFile images = new MockMultipartFile("images", "테스트데이터01.jpg", "multipart/form-data", "테스트데이터01".getBytes());

        mvc.perform(
                multipart("/core/v1/creative/1")
                        .file(images)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "업데이트 테스트")
                        .param("price", "6")
                        .param("advertiseStartDate", "2021-07-24T17:00")
                        .param("advertiseEndDate", "2021-07-25T17:00")
        )
                .andExpect(status().isOk());

        mvc.perform(
                get("/core/v1/creative/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("업데이트 테스트"))
                .andExpect(jsonPath("$.price").value(6))
                .andExpect(jsonPath("$.advertiseStartDate").value("2021-07-24T17:00"))
                .andExpect(jsonPath("$.advertiseEndDate").value("2021-07-25T17:00"));
    }

    @Test
    @DisplayName("소재 수정 성공 테스트 (광고 대기중, 시작 시간 변경)")
    public void updateStartDateWithWaiting() throws Exception {
        String dateFormat = LocalDateTime.now().format(dateTimeFormatter);
        mvc.perform(
                multipart("/core/v1/creative/1")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("advertiseStartDate", dateFormat)
        )
                .andExpect(status().isOk());

        mvc.perform(
                get("/core/v1/creative/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.advertiseStartDate").value(dateFormat))
                .andExpect(jsonPath("$.status").value("ADVERTISING"));
    }

    @Test
    @DisplayName("소재 수정 성공 테스트 (광고 진행중, 끝 시간 변경)")
    public void updateEndDateWithAdvertising() throws Exception{
        String dateFormat = LocalDateTime.now().format(dateTimeFormatter);
        mvc.perform(
                multipart("/core/v1/creative/2")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("advertiseEndDate", dateFormat)
        )
                .andExpect(status().isOk());

        mvc.perform(
                get("/core/v1/creative/2")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.advertiseEndDate").value(dateFormat))
                .andExpect(jsonPath("$.status").value("EXPIRATION"));
    }

    @Test
    @DisplayName("소재 수정 성공 테스트 (광고 진행중, 시작 시간 변경)")
    public void updateStartDateWithAdvertising() throws Exception {
        String dateFormat = LocalDateTime.now().plusMinutes(1L).format(dateTimeFormatter);
        mvc.perform(
                multipart("/core/v1/creative/2")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("advertiseStartDate", dateFormat)
        )
                .andExpect(status().isOk());

        mvc.perform(
                get("/core/v1/creative/2")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.advertiseStartDate").value(dateFormat))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    @DisplayName("소재 수정 성공 테스트 (광고 만료, 끝 시간 변경)")
    public void updateEndDateWithExpiration() throws Exception {
        String dateFormat = LocalDateTime.now().plusMinutes(1L).format(dateTimeFormatter);
        mvc.perform(
                multipart("/core/v1/creative/3")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("advertiseEndDate", dateFormat)
        )
                .andExpect(status().isOk());

        mvc.perform(
                get("/core/v1/creative/3")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.advertiseEndDate").value(dateFormat))
                .andExpect(jsonPath("$.status").value("ADVERTISING"));
    }

    @Test
    @DisplayName("소재 수정 성공 테스트 (광고 만료, 시작 시간 변경)")
    public void updateStartDateWithExpiration() throws Exception {
        String startDateFormat = LocalDateTime.now().plusMinutes(1L).format(dateTimeFormatter);
        String EndDateFormat = LocalDateTime.now().plusMinutes(2L).format(dateTimeFormatter);
        mvc.perform(
                multipart("/core/v1/creative/3")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("advertiseStartDate", startDateFormat)
                        .param("advertiseEndDate", EndDateFormat)
        )
                .andExpect(status().isOk());

        mvc.perform(
                get("/core/v1/creative/3")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.advertiseStartDate").value(startDateFormat))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    @DisplayName("삭제된 소재 수정 실패 테스트")
    public void updateFailWithDeleted() throws Exception {
        mvc.perform(
                multipart("/core/v1/creative/4")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "삭제 테스트")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Access deleted creative is denied"))
                .andExpect(jsonPath("$.status").value(1001));
    }

    @Test
    @DisplayName("소재 수정 실패 테스트 (소재 조회 실패)")
    public void updateFailWithNotFoundCreative() throws Exception {
        mvc.perform(
                multipart("/core/v1/creative/7")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "소재 미존재로 소재 수정 실패 테스트")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Find creative failed"))
                .andExpect(jsonPath("$.status").value(1010));
    }

    @Test
    @DisplayName("소재 수정 실패 테스트 (광고 조회 실패)")
    public void updateFailWithNotFoundAdvertisement() throws Exception {
        mvc.perform(
                multipart("/core/v1/creative/5")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "광고 미존재로 소재 수정 실패 테스트")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Find advertisement failed"))
                .andExpect(jsonPath("$.status").value(1011));
    }

    @Test
    @DisplayName("소재 수정 실패 테스트 (파일 미존재)")
    public void updateFailWithNotFoundFile() throws Exception {
        MockMultipartFile images = new MockMultipartFile("images", "업데이트테스트.jpg", "multipart/form-data", "업데이트테스트".getBytes());

        mvc.perform(
                multipart("/core/v1/creative/5")
                        .file(images)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "업데이트테스트")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("File not found"))
                .andExpect(jsonPath("$.status").value(901));
    }

    @Test
    @DisplayName("소재 삭제 성공 테스트")
    public void deleteSuccess() throws Exception {
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
    @DisplayName("소재 삭제 실패 테스트 (소재 조회 실패)")
    public void deleteFail() throws Exception {
        mvc.perform(
                delete("/core/v1/creative/0")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Find creative failed"))
                .andExpect(jsonPath("$.status").value(1010));
    }

    @Test
    @DisplayName("소재 삭제 실패 테스트 (이미 삭제된 소재)")
    public void deleteFailAlreadyDeleted() throws Exception {
        mvc.perform(
                delete("/core/v1/creative/4")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Access deleted creative is denied"))
                .andExpect(jsonPath("$.status").value(1001));
    }

    @Test
    @DisplayName("단일 소재 조회 성공 테스트")
    public void findOneSuccess() throws Exception {
        mvc.perform(
                get("/core/v1/creative/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트데이터1"))
                .andExpect(jsonPath("$.price").value(1));
    }

    @Test
    @DisplayName("단일 소재 조회 실패 테스트")
    public void findOneFail() throws Exception {
        mvc.perform(
                get("/core/v1/creative/0")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Find creative failed"))
                .andExpect(jsonPath("$.status").value(1010));
    }

    @Test
    @DisplayName("모든 소재 조회 성공 테스트")
    public void findAllSuccess() throws Exception {
        mvc.perform(
                get("/core/v1/creative/")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("테스트데이터1"))
                .andExpect(jsonPath("$[1].title").value("테스트데이터2"));
    }

    @Test
    @DisplayName("소재 일시정지 성공 테스트")
    public void pauseSuccess() throws Exception {
        mvc.perform(
                get("/core/v1/creative/pause/2")
        )
                .andExpect(status().isOk());

        mvc.perform(
                get("/core/v1/creative/2")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAUSE"));
    }

    @Test
    @DisplayName("소재 일시정지 실패 테스트 (삭제된 소재)")
    public void pauseFailDeleted() throws Exception {
        mvc.perform(
                get("/core/v1/creative/pause/4")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Access deleted creative is denied"))
                .andExpect(jsonPath("$.status").value(1001));
    }

    @Test
    @DisplayName("소재 일시정지 실패 테스트 (만료된 소재)")
    public void pauseFailExpired() throws Exception {
        mvc.perform(
                get("/core/v1/creative/pause/3")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Creative was expired"))
                .andExpect(jsonPath("$.status").value(1002));
    }

    @Test
    @DisplayName("소재 일시정지 실패 테스트 (일시정지된 소재)")
    public void pauseFailPaused() throws Exception {
        mvc.perform(
                get("/core/v1/creative/pause/6")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Creative was paused"))
                .andExpect(jsonPath("$.status").value(1003));
    }
}