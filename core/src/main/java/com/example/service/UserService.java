package com.example.service;

import com.example.model.UserEntity;

public interface UserService {
    void saveUser(UserEntity user);

    void updateUser(UserEntity updateUser, Integer id);

    UserEntity getUserById(Integer id);
    UserEntity loadUserByUsername(String email);

}
