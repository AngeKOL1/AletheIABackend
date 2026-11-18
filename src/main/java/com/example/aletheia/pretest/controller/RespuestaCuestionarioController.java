package com.example.aletheia.pretest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.aletheia.pretest.dto.RespuestaCuestionarioLoteDTO;
import com.example.aletheia.pretest.service.IRespuestaCuestionarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/respuestas-cuestionario")
@RequiredArgsConstructor
public class RespuestaCuestionarioController {

    private final IRespuestaCuestionarioService service;

    @PostMapping("/lote")
    public ResponseEntity<Void> registrarLote(
            @Valid @RequestBody RespuestaCuestionarioLoteDTO dto,
            HttpServletRequest request) {
        Integer participanteId = (Integer) request.getAttribute("authenticatedParticipanteId");
        service.registrarLote(participanteId, dto);
        return ResponseEntity.ok().build();
    }
}
