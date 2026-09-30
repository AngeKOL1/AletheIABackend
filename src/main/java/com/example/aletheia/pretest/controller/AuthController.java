package com.example.aletheia.pretest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.aletheia.pretest.dto.LoginRequestDTO;
import com.example.aletheia.pretest.dto.LoginResponseDTO;
import com.example.aletheia.pretest.dto.ParticipanteRegisterDTO;
import com.example.aletheia.pretest.dto.ParticipanteResponseDTO;
import com.example.aletheia.pretest.service.IParticipanteService;
import com.example.aletheia.pretest.service.impl.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final IParticipanteService participanteService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.autenticar(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<ParticipanteResponseDTO> register(@Valid @RequestBody ParticipanteRegisterDTO dto) {
        ParticipanteResponseDTO participante = participanteService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(participante);
    }
    
}
