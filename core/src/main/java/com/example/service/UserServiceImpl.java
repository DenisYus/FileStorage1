package com.example.service;

import com.example.dao.UserRepository;
import com.example.model.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Lazy
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void saveUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setBanCheck(false); //CHECK ME
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(UserEntity updateUser, Integer id) {
        UserEntity user = getUserById(id);
        if (!(user.getPassword()).equals(updateUser.getPassword())) {
            updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));

        }
        userRepository.save(updateUser);
    }

    @Override
    public UserEntity getUserById(Integer id) {
        return userRepository.findById(id).get();
    }

    @Override
    @Transactional
    public UserEntity loadUserByUsername(String email) {
        return userRepository.findByEmail(email);
    }


}
