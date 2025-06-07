package com.digitallly.authservice.controller;

import com.digitallly.authservice.dto.AuthResponse;
import com.digitallly.authservice.dto.UserDto;
import com.digitallly.authservice.service.AuthService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserDto dto) {
        String token = authService.register(dto);
        return ResponseEntity.ok(AuthResponse.builder().token(token).build());
    }

    @PostMapping("/login")
    @PermitAll
    public ResponseEntity<AuthResponse> login(@RequestBody UserDto dto) {
        String token = authService.login(dto);
        return ResponseEntity.ok(AuthResponse.builder().token(token).build());
    }

    @PostMapping("/create-user")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto dto) {
        UserDto savedUserDto = authService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDto);
    }

}
