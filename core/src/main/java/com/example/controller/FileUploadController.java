package com.example.controller;

import com.example.service.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("email") String email,
                                             @RequestParam("files")List<MultipartFile> files){
        fileUploadService.uploadFiles(email, files);
        return ResponseEntity.ok("File uploaded");
    }
}
