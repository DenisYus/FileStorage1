package com.example.service;

import com.example.model.FileEntity;
import com.example.model.UserEntity;

import java.util.List;

public interface FileViewService {
    List<FileEntity> getUserFiles(UserEntity user);
}
