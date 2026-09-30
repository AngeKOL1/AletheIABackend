package com.example.aletheia.pretest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.aletheia.pretest.dto.RespuestaCuestionarioLoteDTO;
import com.example.aletheia.pretest.service.IRespuestaCuestionarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/respuestas-cuestionario")
@RequiredArgsConstructor
public class RespuestaCuestionarioController {

    private final IRespuestaCuestionarioService service;

    @PostMapping("/lote")
    public ResponseEntity<Void> registrarLote(@RequestBody RespuestaCuestionarioLoteDTO dto) {
        service.registrarLote(dto);
        return ResponseEntity.ok().build();
    }
}