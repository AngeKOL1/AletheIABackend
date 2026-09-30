package com.example.aletheia.pretest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aletheia.pretest.dto.NoticiaCreateDTO;
import com.example.aletheia.pretest.dto.NoticiaResponseDTO;
import com.example.aletheia.pretest.service.INoticiaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/noticia")
@RequiredArgsConstructor
public class NoticiaController {

    private final INoticiaService service;

    @PostMapping
    public ResponseEntity<NoticiaResponseDTO> crearMetodo(@Valid @RequestBody NoticiaCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createNoticia(dto));
    }

}
