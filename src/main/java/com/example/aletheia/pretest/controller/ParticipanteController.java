package com.example.aletheia.pretest.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.aletheia.pretest.dto.PageResponseDTO;
import com.example.aletheia.pretest.dto.ParticipanteResponseDTO;
import com.example.aletheia.pretest.service.IParticipanteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/participante")
@RequiredArgsConstructor
public class ParticipanteController {
    private final IParticipanteService service;

    @GetMapping
    public ResponseEntity<PageResponseDTO<ParticipanteResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamano,
            @RequestParam(defaultValue = "id") String ordenarPor) {

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by(ordenarPor));
        return ResponseEntity.ok(service.listarParticipantes(pageable));
    }

}
