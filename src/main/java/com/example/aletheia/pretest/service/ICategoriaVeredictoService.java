package com.example.aletheia.pretest.service;

import java.util.List;

import com.example.aletheia.pretest.dto.CategoriaVeredictoResponseDTO;
import com.example.aletheia.pretest.models.CategoriaVeredicto;

public interface ICategoriaVeredictoService extends IGenericService<CategoriaVeredicto, Integer> {
    List<CategoriaVeredictoResponseDTO> listar();   
}
