package com.example.aletheia.pretest.service.impl;

import org.springframework.stereotype.Service;

import com.example.aletheia.pretest.dto.NoticiaCreateDTO;
import com.example.aletheia.pretest.dto.NoticiaResponseDTO;
import com.example.aletheia.pretest.mapper.NoticiaMapper;
import com.example.aletheia.pretest.models.Noticia;
import com.example.aletheia.pretest.repository.NoticiaRepo;
import com.example.aletheia.pretest.service.INoticiaService;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class NoticiaService extends GenericService<Noticia, Integer> implements INoticiaService {
    private final NoticiaRepo repo;
    private final NoticiaMapper mapper;

    @Override 
    protected NoticiaRepo getRepo() {
        return repo;
    }

    @Override 
    public NoticiaResponseDTO createNoticia(NoticiaCreateDTO noticiaCreateDTO){
        Noticia noticia = mapper.toEntity(noticiaCreateDTO);
        return mapper.toResponseDTO(repo.save(noticia));
    }
}
