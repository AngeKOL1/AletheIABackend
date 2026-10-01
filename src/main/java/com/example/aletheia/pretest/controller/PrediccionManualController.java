package com.example.aletheia.pretest.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.aletheia.pretest.dto.PrediccionManualCreateDTO;
import com.example.aletheia.pretest.dto.PrediccionManualResponseDTO;
import com.example.aletheia.pretest.models.enums.Fase;
import com.example.aletheia.pretest.service.IPrediccionManualService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/predicciones-manuales")
@RequiredArgsConstructor
public class PrediccionManualController {

    private final IPrediccionManualService service;

    @PostMapping
    public ResponseEntity<PrediccionManualResponseDTO> registrar(
            @Valid @RequestBody PrediccionManualCreateDTO dto,
            HttpServletRequest request) {
        Integer participanteId = (Integer) request.getAttribute("authenticatedParticipanteId");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(participanteId, dto));
    }

    @GetMapping("/mis-respuestas")
    public List<PrediccionManualResponseDTO> misRespuestas(
            @RequestParam Fase fase,
            HttpServletRequest request) {
        Integer participanteId = (Integer) request.getAttribute("authenticatedParticipanteId");
        return service.listarPorParticipanteYFase(participanteId, fase);
    }

    @GetMapping("/mi-progreso")
    public long miProgreso(@RequestParam Fase fase, HttpServletRequest request) {
        Integer participanteId = (Integer) request.getAttribute("authenticatedParticipanteId");
        return service.contarRespondidas(participanteId, fase);
    }

    @GetMapping("/mis-ids-respondidos")
    public List<Integer> misIdsRespondidos(@RequestParam Fase fase, HttpServletRequest request) {
        Integer participanteId = (Integer) request.getAttribute("authenticatedParticipanteId");
        return service.obtenerIdsNoticiasRespondidas(participanteId, fase);
    }
}