package com.example.aletheia.pretest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aletheia.pretest.dto.NoticiaCreateDTO;
import com.example.aletheia.pretest.dto.NoticiaFeedDTO;
import com.example.aletheia.pretest.dto.NoticiaResponseDTO;
import com.example.aletheia.pretest.service.INoticiaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/noticias")
@RequiredArgsConstructor
public class NoticiaController {

    private final INoticiaService service;

    @PostMapping
    public ResponseEntity<NoticiaResponseDTO> crearMetodo(@Valid @RequestBody NoticiaCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createNoticia(dto));
    }

    @GetMapping
    public List<NoticiaResponseDTO> listarTodas() {
        return service.listarTodas();
    }

    @GetMapping("/feed")
    public List<NoticiaFeedDTO> feed(HttpServletRequest request) {
        Integer participanteId = (Integer) request.getAttribute("authenticatedParticipanteId");
        return service.obtenerFeedParaParticipante(participanteId);
    }

    @PostMapping("/lote")
    public ResponseEntity<List<NoticiaResponseDTO>> crearEnLote(
            @RequestBody @NotEmpty @Size(max = 500) List<@Valid NoticiaCreateDTO> dtos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearEnLote(dtos));
    }
}
