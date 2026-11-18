package com.example.aletheia.pretest.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.aletheia.pretest.dto.EstadoParticipanteDTO;
import com.example.aletheia.pretest.dto.PageResponseDTO;
import com.example.aletheia.pretest.dto.ParticipanteResponseDTO;
import com.example.aletheia.pretest.models.enums.Fase;
import com.example.aletheia.pretest.service.IParticipanteService;

import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/participantes")
@RequiredArgsConstructor
public class ParticipanteController {
    private final IParticipanteService service;

    // Campos por los que se permite ordenar (nunca por contrasenaHash u otros internos).
    private static final Set<String> CAMPOS_ORDENABLES =
            Set.of("id", "nombres", "apellidos", "correo", "edad", "fechaRegistro");

    @GetMapping
    public ResponseEntity<PageResponseDTO<ParticipanteResponseDTO>> listar(
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int tamano,
            @RequestParam(defaultValue = "id") String ordenarPor) {

        if (!CAMPOS_ORDENABLES.contains(ordenarPor)) {
            throw new IllegalArgumentException("No se puede ordenar por '" + ordenarPor
                    + "'. Valores permitidos: " + CAMPOS_ORDENABLES);
        }

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by(ordenarPor));
        return ResponseEntity.ok(service.listarParticipantes(pageable));
    }

    @GetMapping("/me/estado")
    public EstadoParticipanteDTO miEstado(@RequestParam Fase fase, HttpServletRequest request) {
        Integer participanteId = (Integer) request.getAttribute("authenticatedParticipanteId");
        return service.obtenerEstado(participanteId, fase);
    }

    
}
