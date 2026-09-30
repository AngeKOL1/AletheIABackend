package com.example.aletheia.pretest.controller;

import org.springframework.web.bind.annotation.*;
import com.example.aletheia.pretest.models.SesionTest;
import com.example.aletheia.pretest.models.enums.Fase;
import com.example.aletheia.pretest.service.ISesionTestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sesiones-test")
@RequiredArgsConstructor
public class SesionTestController {

    private final ISesionTestService service;

    @PostMapping("/iniciar")
    public SesionTest iniciar(@RequestParam Fase fase, @RequestParam String titulo, HttpServletRequest request) {
        Integer participanteId = (Integer) request.getAttribute("authenticatedParticipanteId");
        return service.iniciar(participanteId, fase, titulo);
    }

    @PutMapping("/{id}/finalizar")
    public SesionTest finalizar(@PathVariable Integer id) {
        return service.finalizar(id);
    }
}