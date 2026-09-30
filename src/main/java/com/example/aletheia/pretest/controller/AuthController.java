package com.example.aletheia.pretest.controller;

import org.springframework.web.bind.annotation.*;

import com.example.aletheia.pretest.dto.LoginRequestDTO;
import com.example.aletheia.pretest.dto.LoginResponseDTO;
import com.example.aletheia.pretest.service.impl.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        return authService.autenticar(dto);
    }
}
