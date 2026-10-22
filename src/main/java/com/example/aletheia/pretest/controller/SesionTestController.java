package com.example.aletheia.pretest.controller;

import org.springframework.web.bind.annotation.*;

import com.example.aletheia.pretest.dto.SesionTestResponseDTO;
import com.example.aletheia.pretest.models.enums.Fase;
import com.example.aletheia.pretest.service.ISesionTestService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sesiones-test")
@RequiredArgsConstructor
public class SesionTestController {

    private final ISesionTestService service;

    @PostMapping("/iniciar")
    public SesionTestResponseDTO iniciar(
            @RequestParam Fase fase,
            @RequestParam @NotBlank @Size(max = 255) String titulo,
            HttpServletRequest request) {
        Integer participanteId = (Integer) request.getAttribute("authenticatedParticipanteId");
        return service.iniciar(participanteId, fase, titulo);
    }

    @PutMapping("/{id}/finalizar")
    public SesionTestResponseDTO finalizar(@PathVariable Integer id, HttpServletRequest request) {
        Integer participanteId = (Integer) request.getAttribute("authenticatedParticipanteId");
        return service.finalizar(participanteId, id);
    }
}
