package com.example.aletheia.pretest.service;

import java.util.List;

import com.example.aletheia.pretest.dto.ItemCuestionarioResponseDTO;
import com.example.aletheia.pretest.models.ItemCuestionario;
import com.example.aletheia.pretest.models.enums.Fase;

public interface IItemCuestionarioService extends IGenericService<ItemCuestionario, Integer> {
    List<ItemCuestionarioResponseDTO> listar (Fase fase);
}
