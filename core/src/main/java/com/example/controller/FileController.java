package com.example.controller;

import com.example.dto.FileDto;
import com.example.mapers.FileMapper;
import com.example.model.FileEntity;
import com.example.model.UserEntity;
import com.example.service.FileDownloadService;
import com.example.service.FileUploadService;
import com.example.service.FileViewService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileUploadService fileUploadService;
    private final FileViewService fileViewService;
    private final FileDownloadService fileDownloadService;

    public FileController(FileUploadService fileUploadService, FileViewService fileViewService, FileDownloadService fileDownloadService) {
        this.fileUploadService = fileUploadService;
        this.fileViewService = fileViewService;
        this.fileDownloadService = fileDownloadService;
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("email") String email,
                                             @RequestParam("files")List<MultipartFile> files){
        fileUploadService.uploadFiles(email, files);
        return ResponseEntity.ok("File uploaded");
    }
    @GetMapping("/user-files")
    public ResponseEntity<List<FileDto>> getUserFiles(@AuthenticationPrincipal UserEntity user){
        List<FileEntity> filesEntity = fileViewService.getUserFiles(user);
        List<FileDto> fileDtos = FileMapper.INSTANCE.toDtoList(filesEntity);
        return ResponseEntity.ok(fileDtos);
    }
    @GetMapping("/download/{fileName}")
    public  ResponseEntity<byte[]> downloadFile(@AuthenticationPrincipal UserEntity user,
                                                @PathVariable String fileName){
        try {
            byte[] fileData = fileDownloadService.downloadFile(fileName, user);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+fileName+
                            "\"").body(fileData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
