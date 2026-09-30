package com.example.aletheia.pretest.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.example.aletheia.pretest.dto.ItemCuestionarioResponseDTO;
import com.example.aletheia.pretest.mapper.ItemCuestionarioMapper;
import com.example.aletheia.pretest.repository.ItemCuestionarioRepo;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/items-cuestionario")
@RequiredArgsConstructor
public class ItemCuestionarioController {

    private final ItemCuestionarioRepo repo;
    private final ItemCuestionarioMapper mapper;

    @GetMapping
    public List<ItemCuestionarioResponseDTO> listar() {
        return mapper.toResponseDTOList(repo.findAllByOrderByOrdenAsc());
    }
}