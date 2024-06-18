package com.example.valodator;

import com.example.exception.FileExtensionException;
import com.example.exception.FileNameException;
import com.example.exception.FileSizeException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Component
public class FileUploadValidator {
    private final List<String> ALLOW_EXTENSIONS = Arrays.asList("png", "jpg");
    private final long MAX_FILE_SIZE = 10*1024*1024; //10mb
    public void validate (MultipartFile file){
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()){
            throw new FileNameException("File name should not be empty");
        }
        String fileExtension = getFileExtensive(fileName);
        if (!ALLOW_EXTENSIONS.contains(fileExtension)){
            throw new FileExtensionException("File extension should be .pnd or .jpg");
        }
        if (file.getSize() > MAX_FILE_SIZE){
            throw new FileSizeException("File size should not be more than 10MB");
        }


    }
    private String getFileExtensive (String fileName){
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex+1).toLowerCase();
    }

}
