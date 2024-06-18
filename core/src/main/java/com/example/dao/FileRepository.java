package com.example.dao;


import com.example.model.FileEntity;
import com.example.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {
    List<FileEntity> findByUser(UserEntity user);

}
