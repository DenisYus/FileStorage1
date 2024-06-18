package com.example.service;

import com.example.dao.FileRepository;
import com.example.dao.UserRepository;
import com.example.exception.FileSaveException;
import com.example.model.FileEntity;
import com.example.model.UserEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class FileUploadServiceImpl implements FileUploadService{
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public FileUploadServiceImpl(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }
    private String uploadDir = "upload/";
    @Override
    public void uploadFiles(String email, List<MultipartFile> files) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        for (MultipartFile file: files){
            saveFile(file,user);
        }
    }
    private void saveFile(MultipartFile file, UserEntity user){
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir+fileName);
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new FileSaveException("Failed to save file");
        }
        FileEntity fileEntity = FileEntity.builder()
                .name(fileName)
                .size(file.getSize())
                .uploadData(LocalDateTime.now())
                .user(user)
                .build();
        fileRepository.save(fileEntity);

    }
}
