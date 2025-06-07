package com.digitallly.authservice.service;

import com.digitallly.authservice.domain.User;
import com.digitallly.authservice.dto.UserDto;
import com.digitallly.authservice.enums.UserRole;
import com.digitallly.authservice.repository.UserRepository;
import com.digitallly.authservice.utils.JwtUtil;
import com.digitallly.authservice.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    public String register(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null ? dto.getRole() : UserRole.USER);

        userRepository.save(user);

        return jwtUtil.generateToken(user);
    }

    public UserDto createUser(UserDto dto) {
        if (dto.getRole() == UserRole.SUPER_ADMIN) {
            throw new IllegalArgumentException("SUPER_ADMIN can't be created");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        String generatedPassword = passwordUtil.generatePassword();

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setPassword(encoder.encode(generatedPassword));
        user.setPasswordChanged(false);

        userRepository.save(user);

        dto.setPassword(generatedPassword);

        return dto;
    }

    public String login(UserDto dto) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            User user = (User) auth.getPrincipal();
            return jwtUtil.generateToken(user);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

}
