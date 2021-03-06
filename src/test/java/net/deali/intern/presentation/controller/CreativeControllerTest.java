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
                .title("??????????????????2")
                .price(2L)
                .image("??????????????????02.txt")
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

        // API??? ???????????? ????????????????????? ?????? ??? ?????????, ???????????? ??????
        // POST API??? ??????????????? ???????????? ???????????? ??????????????? POST API??? ???????????????.
        MockMultipartFile images1 = new MockMultipartFile("images", "??????????????????01.jpg", "multipart/form-data", "??????????????????01".getBytes());

        Creative creative1 = creativeRepository.findById(1L)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
        List<CreativeImage> creativeImage1 = creative1.getCreativeImages();

        String filename1 = StringUtils.cleanPath(creativeImage1.get(0).getId() + "." + creativeImage1.get(0).getExtension());
        Path targetPath1 = directory.resolve(filename1).normalize();

        images1.transferTo(targetPath1);

        MockMultipartFile images2 = new MockMultipartFile("images", "??????????????????02.jpg", "multipart/form-data", "??????????????????02".getBytes());

        Creative creative2 = creativeRepository.findById(2L)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
        List<CreativeImage> creativeImage2 = creative2.getCreativeImages();

        String filename2 = StringUtils.cleanPath(creativeImage2.get(0).getId() + "." + creativeImage2.get(0).getExtension());
        Path targetPath2 = directory.resolve(filename2).normalize();

        images2.transferTo(targetPath2);

        MockMultipartFile images3 = new MockMultipartFile("images", "??????????????????03.jpg", "multipart/form-data", "??????????????????03".getBytes());

        Creative creative3 = creativeRepository.findById(3L)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
        List<CreativeImage> creativeImage3 = creative3.getCreativeImages();

        String filename3 = StringUtils.cleanPath(creativeImage3.get(0).getId() + "." + creativeImage3.get(0).getExtension());
        Path targetPath3 = directory.resolve(filename3).normalize();

        images3.transferTo(targetPath3);

        MockMultipartFile images4 = new MockMultipartFile("images", "??????????????????04.jpg", "multipart/form-data", "??????????????????04".getBytes());

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
    @DisplayName("?????? ?????? ?????? ?????????")
    public void createSuccess(@Autowired CreativeRepository creativeRepository) throws Exception {
        MockMultipartFile images = new MockMultipartFile("images", "???????????????.jpg", "multipart/form-data", "???????????????".getBytes());

        mvc.perform(
                multipart("/core/v1/creative/")
                        .file(images)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "???????????????")
                        .param("price", "5")
                        .param("advertiseStartDate", "2021-07-24T16:00")
                        .param("advertiseEndDate", "2021-07-25T16:00"))
        .andExpect(status().isCreated());

        mvc.perform(
                get("/core/v1/creative/7")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("???????????????"))
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
    @DisplayName("?????? ?????? ?????? ?????????")
    public void createFail() throws Exception {
        mvc.perform(
                multipart("/core/v1/creative/")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Invalid input value"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ????????? (?????? ??????)")
    public void createFailDate() throws Exception {
        MockMultipartFile images = new MockMultipartFile("images", "???????????????.jpg", "multipart/form-data", "???????????????".getBytes());
        mvc.perform(
                multipart("/core/v1/creative/")
                        .file(images)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "???????????????")
                        .param("price", "5")
                        .param("advertiseStartDate", "2021-07-28T16:00")
                        .param("advertiseEndDate", "2021-07-25T16:00"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Invalid input date"))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ????????? (????????? ?????? ??????)")
    public void updateWithImageSuccess() throws Exception {
        MockMultipartFile images = new MockMultipartFile("images", "?????????????????????.jpg", "multipart/form-data", "?????????????????????".getBytes());

        mvc.perform(
                multipart("/core/v1/creative/1")
                .file(images)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .param("title", "???????????? ?????????")
                .param("price", "6")
                .param("advertiseStartDate", "2021-07-24T17:00")
                .param("advertiseEndDate", "2021-07-25T17:00")
        )
                .andExpect(status().isOk());

        mvc.perform(
                get("/core/v1/creative/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("???????????? ?????????"))
                .andExpect(jsonPath("$.price").value(6))
                .andExpect(jsonPath("$.advertiseStartDate").value("2021-07-24T17:00"))
                .andExpect(jsonPath("$.advertiseEndDate").value("2021-07-25T17:00"));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ????????? (????????? ?????????)")
    public void updateWithoutImageSuccess() throws Exception {
        mvc.perform(
                multipart("/core/v1/creative/1")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "???????????? ?????????")
                        .param("price", "6")
        )
                .andExpect(status().isOk());

        mvc.perform(
                get("/core/v1/creative/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("???????????? ?????????"))
                .andExpect(jsonPath("$.price").value(6))
                .andExpect(jsonPath("$.advertiseStartDate").value("2021-07-24T17:00"))
                .andExpect(jsonPath("$.advertiseEndDate").value("2021-07-27T17:00"));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ????????? (????????? ????????? ??????)")
    public void updateWithSameImageSuccess() throws Exception {
        MockMultipartFile images = new MockMultipartFile("images", "??????????????????01.jpg", "multipart/form-data", "??????????????????01".getBytes());

        mvc.perform(
                multipart("/core/v1/creative/1")
                        .file(images)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "???????????? ?????????")
                        .param("price", "6")
                        .param("advertiseStartDate", "2021-07-24T17:00")
                        .param("advertiseEndDate", "2021-07-25T17:00")
        )
                .andExpect(status().isOk());

        mvc.perform(
                get("/core/v1/creative/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("???????????? ?????????"))
                .andExpect(jsonPath("$.price").value(6))
                .andExpect(jsonPath("$.advertiseStartDate").value("2021-07-24T17:00"))
                .andExpect(jsonPath("$.advertiseEndDate").value("2021-07-25T17:00"));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ????????? (?????? ?????????, ?????? ?????? ??????)")
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
    @DisplayName("?????? ?????? ?????? ????????? (?????? ?????????, ??? ?????? ??????)")
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
    @DisplayName("?????? ?????? ?????? ????????? (?????? ?????????, ?????? ?????? ??????)")
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
    @DisplayName("?????? ?????? ?????? ????????? (?????? ??????, ??? ?????? ??????)")
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
    @DisplayName("?????? ?????? ?????? ????????? (?????? ??????, ?????? ?????? ??????)")
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
    @DisplayName("????????? ?????? ?????? ?????? ?????????")
    public void updateFailWithDeleted() throws Exception {
        mvc.perform(
                multipart("/core/v1/creative/4")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "?????? ?????????")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Access deleted creative is denied"))
                .andExpect(jsonPath("$.status").value(1001));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ????????? (?????? ?????? ??????)")
    public void updateFailWithNotFoundCreative() throws Exception {
        mvc.perform(
                multipart("/core/v1/creative/7")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "?????? ???????????? ?????? ?????? ?????? ?????????")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Find creative failed"))
                .andExpect(jsonPath("$.status").value(1010));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ????????? (?????? ?????? ??????)")
    public void updateFailWithNotFoundAdvertisement() throws Exception {
        mvc.perform(
                multipart("/core/v1/creative/5")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "?????? ???????????? ?????? ?????? ?????? ?????????")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Find advertisement failed"))
                .andExpect(jsonPath("$.status").value(1011));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ????????? (?????? ?????????)")
    public void updateFailWithNotFoundFile() throws Exception {
        MockMultipartFile images = new MockMultipartFile("images", "?????????????????????.jpg", "multipart/form-data", "?????????????????????".getBytes());

        mvc.perform(
                multipart("/core/v1/creative/5")
                        .file(images)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "?????????????????????")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("File not found"))
                .andExpect(jsonPath("$.status").value(901));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    public void deleteSuccess() throws Exception {
        mvc.perform(
                delete("/core/v1/creative/2")
        )
                .andExpect(status().isOk());

        // Transactional??? ?????? ???????????? DELETE ????????? ????????? DB?????? ???????????? ?????? ????????? ?????????????????? ????????? ??????.
        // ??? ???????????? ????????? DB??? ???????????? ????????? DELETED??? ???????????? ?????? ??????
        // @Transactional??? ???????????? ????????? ?????? ??????
        // Service?????? ??????????????? ?????????????????? ?????? ????????? ???????????????????????? ?????? ????????? ????????? ??????????????????
        mvc.perform(
                get("/core/v1/creative/2")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELETED"));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ????????? (?????? ?????? ??????)")
    public void deleteFail() throws Exception {
        mvc.perform(
                delete("/core/v1/creative/0")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Find creative failed"))
                .andExpect(jsonPath("$.status").value(1010));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ????????? (?????? ????????? ??????)")
    public void deleteFailAlreadyDeleted() throws Exception {
        mvc.perform(
                delete("/core/v1/creative/4")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Access deleted creative is denied"))
                .andExpect(jsonPath("$.status").value(1001));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    public void findOneSuccess() throws Exception {
        mvc.perform(
                get("/core/v1/creative/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("??????????????????1"))
                .andExpect(jsonPath("$.price").value(1));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    public void findOneFail() throws Exception {
        mvc.perform(
                get("/core/v1/creative/0")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Find creative failed"))
                .andExpect(jsonPath("$.status").value(1010));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    public void findAllSuccess() throws Exception {
        mvc.perform(
                get("/core/v1/creative/")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("??????????????????1"))
                .andExpect(jsonPath("$[1].title").value("??????????????????2"));
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ?????????")
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
    @DisplayName("?????? ???????????? ?????? ????????? (????????? ??????)")
    public void pauseFailDeleted() throws Exception {
        mvc.perform(
                get("/core/v1/creative/pause/4")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Access deleted creative is denied"))
                .andExpect(jsonPath("$.status").value(1001));
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ????????? (????????? ??????)")
    public void pauseFailExpired() throws Exception {
        mvc.perform(
                get("/core/v1/creative/pause/3")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Creative was expired"))
                .andExpect(jsonPath("$.status").value(1002));
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ????????? (??????????????? ??????)")
    public void pauseFailPaused() throws Exception {
        mvc.perform(
                get("/core/v1/creative/pause/6")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Creative was paused"))
                .andExpect(jsonPath("$.status").value(1003));
    }
}