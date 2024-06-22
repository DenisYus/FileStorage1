package com.example.service;

import com.example.dao.FileRepository;
import com.example.dao.UserRepository;
import com.example.dto.FileFilterDto;
import com.example.exception.FileNotFoundException;
import com.example.exception.FileSaveException;
import com.example.model.FileEntity;
import com.example.model.UserEntity;
import com.example.valodator.FileUploadValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Value("${file.storage.location}")
    private String fileStorageLocation;
    @Value("${upload.Directory}")
    private String uploadDir;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FileUploadValidator fileUploadValidator;

    public FileServiceImpl(FileRepository fileRepository, UserRepository userRepository, FileUploadValidator fileUploadValidator) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.fileUploadValidator = fileUploadValidator;
    }

    @Override
    public byte[] downloadFile(String fileName, UserEntity user) throws IOException {
        String userDirectory = fileStorageLocation + File.separator + user.getId();
        File file = new File(userDirectory, fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found");
        }

        return Files.readAllBytes(file.toPath());
    }

    @Override
    public void uploadFiles(String email, List<MultipartFile> files) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        for (MultipartFile file : files) {
            fileUploadValidator.validate(file);
            saveFile(file, user);
        }
    }

    private void saveFile(MultipartFile file, UserEntity user) {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new FileSaveException("Failed to save file");
        }
        FileEntity fileEntity = FileEntity.builder()
                .name(fileName)
                .size(file.getSize())
                .uploadDate(LocalDateTime.now())
                .user(user)
                .build();
        fileRepository.save(fileEntity);
    }

    @Override
    public List<FileEntity> getUserFiles(UserEntity user) {
        return fileRepository.findByUser(user);
    }

    @Override
    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    @Override
    public List<FileEntity> getUserFiles(UserEntity user, FileFilterDto filterDto) {
        return applyFiltersAndSort(fileRepository.findByUser(user), filterDto);
    }

    @Override
    public List<FileEntity> getAllFiles(FileFilterDto filterDto) {
        return applyFiltersAndSort(fileRepository.findAll(), filterDto);
    }

    private List<FileEntity> applyFiltersAndSort(List<FileEntity> files, FileFilterDto filterDto) {
        List<FileEntity> filteredFiles = new ArrayList<>(files);
        if (filterDto.getUploadDateFrom() != null) {
            filteredFiles.removeIf(file -> file.getUploadDate().isBefore(filterDto.getUploadDateFrom()));
        }
        if (filterDto.getUploadDateTo() != null) {
            filteredFiles.removeIf(file -> file.getUploadDate().isAfter(filterDto.getUploadDateTo()));
        }
        if (filterDto.getFileIdMin() != null) {
            filteredFiles.removeIf(file -> file.getId() < filterDto.getFileIdMin());
        }
        if (filterDto.getFileIdMax() != null) {
            filteredFiles.removeIf(file -> file.getId() > filterDto.getFileIdMax());
        }
        if (filterDto.getSizeMin() != null) {
            filteredFiles.removeIf(file -> file.getSize() < filterDto.getSizeMin());
        }
        if (filterDto.getSizeMax() != null) {
            filteredFiles.removeIf(file -> file.getSize() > filterDto.getSizeMax());
        }
        if (filterDto.getSortBy() != null && filterDto.getSortDirection() != null) {
            Sort.Direction direction = Sort.Direction.fromString(filterDto.getSortDirection());
            switch (filterDto.getSortBy()) {
                case "id":
                    filteredFiles.sort((a, b) -> direction.isAscending() ? a.getId().compareTo(b.getId()) : b.getId().compareTo(a.getId()));
                    break;
                case "size":
                    filteredFiles.sort((a, b) -> direction.isAscending() ? Long.compare(a.getSize(), b.getSize()) : Long.compare(b.getSize(), a.getSize()));
                    break;
                case "uploadDate":
                    filteredFiles.sort((a, b) -> direction.isAscending() ? a.getUploadDate().compareTo(b.getUploadDate()) : b.getUploadDate().compareTo(a.getUploadDate()));
                    break;
            }
        }
        return filteredFiles;
    }
}
