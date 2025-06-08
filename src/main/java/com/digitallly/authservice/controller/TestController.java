package com.digitallly.authservice.controller;

import jakarta.annotation.security.PermitAll;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/test")
public class TestController {

    @GetMapping(path = "/public")
    @PermitAll
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.status(HttpStatus.OK).body("Public Endpoint");
    }

    @GetMapping(path = "/private")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> privateEndpoint() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        GrantedAuthority role = userDetails.getAuthorities().stream().toList().getFirst();

        return ResponseEntity.status(HttpStatus.OK).body("Private Endpoint. User role is " + role.getAuthority());
    }

}
