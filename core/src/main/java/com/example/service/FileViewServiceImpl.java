package com.example.service;

import com.example.dao.FileRepository;
import com.example.model.FileEntity;
import com.example.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileViewServiceImpl implements FileViewService{
    private final FileRepository fileRepository;

    public FileViewServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public List<FileEntity> getUserFiles(UserEntity user) {
        return fileRepository.findByUser(user);
    }
}
