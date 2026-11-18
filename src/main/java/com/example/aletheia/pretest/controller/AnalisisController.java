package com.example.aletheia.pretest.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aletheia.pretest.dto.AnalisisResponseDTO;
import com.example.aletheia.pretest.dto.AnalisisResumenDTO;
import com.example.aletheia.pretest.service.IAnalisisService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

/** Análisis de noticias con AletheIA. El navegador solo habla con este backend. */
@RestController
@RequestMapping("/api/analisis")
@RequiredArgsConstructor
public class AnalisisController {

    private final IAnalisisService service;

    /** Pide el análisis y responde enseguida (202); el resultado se consulta con el GET. */
    @PostMapping("/noticias/{noticiaId}")
    public ResponseEntity<AnalisisResponseDTO> solicitar(@PathVariable @Positive Integer noticiaId,
            HttpServletRequest request) {
        Integer participanteId = (Integer) request.getAttribute("authenticatedParticipanteId");
        AnalisisResponseDTO analisis = service.solicitar(participanteId, noticiaId);
        return ResponseEntity.accepted()
                .location(URI.create("/api/analisis/noticias/" + noticiaId))
                .body(analisis);
    }

    @GetMapping("/noticias/{noticiaId}")
    public AnalisisResponseDTO consultar(@PathVariable @Positive Integer noticiaId, HttpServletRequest request) {
        Integer participanteId = (Integer) request.getAttribute("authenticatedParticipanteId");
        return service.consultar(participanteId, noticiaId);
    }

    @GetMapping
    public List<AnalisisResumenDTO> misAnalisis(HttpServletRequest request) {
        Integer participanteId = (Integer) request.getAttribute("authenticatedParticipanteId");
        return service.listarMisAnalisis(participanteId);
    }
}
