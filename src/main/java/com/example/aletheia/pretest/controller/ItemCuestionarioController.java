package com.example.aletheia.pretest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.aletheia.pretest.dto.ItemCuestionarioResponseDTO;
import com.example.aletheia.pretest.models.enums.Fase;
import com.example.aletheia.pretest.service.IItemCuestionarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/items-cuestionario")
@RequiredArgsConstructor
public class ItemCuestionarioController {

    private final IItemCuestionarioService service;

    @GetMapping
    public ResponseEntity<List<ItemCuestionarioResponseDTO>> listar(@RequestParam Fase fase) {
        return ResponseEntity.ok(service.listar(fase));
    }
}