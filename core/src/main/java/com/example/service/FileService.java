package com.example.service;

import com.example.dto.FileFilterDto;
import com.example.model.FileEntity;
import com.example.model.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    byte[] downloadFile(String fileName, UserEntity user) throws IOException;
    void uploadFiles(String email, List<MultipartFile> files);
    List<FileEntity> getUserFiles(UserEntity user);
    List<FileEntity> getAllFiles();
    List<FileEntity> getUserFiles(UserEntity user, FileFilterDto filterDto);
    List<FileEntity> getAllFiles(FileFilterDto filterDto);
}
