package com.example.service;

import com.example.model.UserEntity;

import java.io.IOException;

public interface FileDownloadService {
    byte[] downloadFile(String fileName, UserEntity user) throws IOException;

}
