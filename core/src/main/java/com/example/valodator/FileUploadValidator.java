package com.example.valodator;

import com.example.exception.FileExtensionException;
import com.example.exception.FileNameException;
import com.example.exception.FileSizeException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Component
public class FileUploadValidator {
    @Value("${file.allow.extensions}")
    private String allowedExtensions;
    @Value("${file.max.size}")
    private long maxFileSize;
    private List<String> allowExtensionsList;
    @PostConstruct
    public void init(){
        allowExtensionsList = Arrays.asList(allowedExtensions.split(","));
    }


    public void validate (MultipartFile file){
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()){
            throw new FileNameException("File name should not be empty");
        }
        String fileExtension = getFileExtensive(fileName);
        if (!allowExtensionsList.contains(fileExtension)){
            throw new FileExtensionException("File extension should be .pnd or .jpg");
        }
        if (file.getSize() > maxFileSize){
            throw new FileSizeException("File size should not be more than 10MB");
        }


    }
    private String getFileExtensive (String fileName){
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex+1).toLowerCase();
    }

}
