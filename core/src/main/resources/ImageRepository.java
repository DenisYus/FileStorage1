package com.example.dao;

import com.example.model.ImageEntity;
import com.example.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
    List<ImageEntity> findByUser(UserEntity user);

}
