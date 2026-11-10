package com.example.aletheia.pretest.service.impl;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.aletheia.pretest.dto.CategoriaVeredictoResponseDTO;
import com.example.aletheia.pretest.mapper.CategoriaVeredictoMapper;
import com.example.aletheia.pretest.models.CategoriaVeredicto;
import com.example.aletheia.pretest.repository.CategoriaVeredictoRepo;
import com.example.aletheia.pretest.service.ICategoriaVeredictoService;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor
@Transactional(readOnly = true)
public class CategoriaVeredictoService extends GenericService<CategoriaVeredicto, Integer> implements ICategoriaVeredictoService {

    protected final CategoriaVeredictoRepo repo;
    protected final CategoriaVeredictoMapper mapper;
    
    @Override 
    protected CategoriaVeredictoRepo getRepo(){
        return repo;
    }

    @Override
    public List<CategoriaVeredictoResponseDTO> listar(){
        return mapper.toResponseDTOList(repo.findAll(Sort.by("id")));
    }
}
