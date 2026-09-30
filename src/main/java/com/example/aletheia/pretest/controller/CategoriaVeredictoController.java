package com.example.aletheia.pretest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.aletheia.pretest.dto.CategoriaVeredictoResponseDTO;
import com.example.aletheia.pretest.service.ICategoriaVeredictoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaVeredictoController {

    private final ICategoriaVeredictoService service;

    @GetMapping
    public ResponseEntity<List<CategoriaVeredictoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}