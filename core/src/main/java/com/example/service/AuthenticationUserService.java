package com.example.service;

import com.example.dao.UserRepository;
import com.example.exception.UserHasBeenBannedException;
import com.example.model.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Lazy
    public AuthenticationUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        } else if (user.getStatus().equals(UserStatus.BLOCKED)) {
            throw new UserHasBeenBannedException("User has been banned");
        }
        return new User(user.getEmail(), user.getPassword(), user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getUserRole())).toList());
    }
}
