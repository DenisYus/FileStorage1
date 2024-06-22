package com.example.service;

import com.example.exception.FileNotFoundException;
import com.example.model.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
@Service

public class FileDownloadServiceImpl implements  FileDownloadService{
    @Value("${file.storage.location}")
    private String fileStorageLocation;
    @Override
    public byte[] downloadFile(String fileName, UserEntity user) throws IOException {
        String userDirectory = fileStorageLocation + File.separator +user.getId();
        File file = new File(userDirectory, fileName);
        if (!file.exists()){
            throw new FileNotFoundException("File not found");
        }

        return Files.readAllBytes(file.toPath());
    }
}
