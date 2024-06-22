package com.example.controller;

import com.example.dto.FileDto;
import com.example.dto.FileFilterDto;
import com.example.mapers.FileMapper;
import com.example.model.FileEntity;
import com.example.model.UserEntity;
import com.example.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    public final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("email") String email,
                                             @RequestParam("files")List<MultipartFile> files){
        fileService.uploadFiles(email, files);
        return ResponseEntity.ok("File uploaded");
    }
    @GetMapping("/user-files")
    public ResponseEntity<List<FileDto>> getUserFiles(@AuthenticationPrincipal UserEntity user, @RequestBody FileFilterDto filterDto){
        List<FileDto> fileDtos = FileMapper.INSTANCE.toDtoList(fileService.getUserFiles(user, filterDto));
        return ResponseEntity.ok(fileDtos);
    }
    @GetMapping("/all-files")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<List<FileDto>> getAllFiles (@RequestBody FileFilterDto filterDto){
        List<FileDto> fileDtos = FileMapper.INSTANCE.toDtoList(fileService.getAllFiles(filterDto));
        return new ResponseEntity<>(fileDtos, HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public  ResponseEntity<byte[]> downloadFile(@AuthenticationPrincipal UserEntity user,
                                                @PathVariable String fileName){
        try {
            byte[] fileData = fileService.downloadFile(fileName, user);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+fileName+
                            "\"").body(fileData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
