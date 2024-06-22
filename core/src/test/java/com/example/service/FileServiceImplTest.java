package com.example.service;

import com.example.BaseIntegrationTest;
import com.example.dao.FileRepository;
import com.example.dao.UserRepository;
import com.example.model.FileEntity;
import com.example.model.UserEntity;
import com.example.model.UserStatus;
import com.example.valodator.FileUploadValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileServiceImplTest extends BaseIntegrationTest {
    @Autowired
    private FileService fileService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileUploadValidator fileUploadValidator;
    private UserEntity testUser;
    @BeforeEach
    void setUp(){
        testUser = userRepository.save(UserEntity.builder()
                .email("test@mail.ru").password("123").status(UserStatus.UNBAN).build());
    }

    @Test
    void downloadFile() throws IOException {
        //given
        String fileName = "testText.png";
        byte[] content = "This picture text".getBytes();
        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "text/picture", content);
        fileService.uploadFiles(testUser.getEmail(), List.of(multipartFile));
        //when
        byte[] downloadedContent = fileService.downloadFile(fileName, testUser);
        //then
        assertEquals(content, downloadedContent);
    }

    @Test
    void uploadFiles() {
        //given
        String fileName = "testText.png";
        byte[] content = "This picture text".getBytes();
        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "text/picture", content);
        //when
        fileService.uploadFiles(testUser.getEmail(), List.of(multipartFile));
        //then
        List<FileEntity> fileEntities = fileRepository.findByUser(testUser);
        assertEquals(1, fileEntities.size());
        assertEquals(fileName, fileEntities.get(0).getName());
    }

    @Test
    void getUserFiles() {
    }

    @Test
    void getAllFiles() {
    }
}