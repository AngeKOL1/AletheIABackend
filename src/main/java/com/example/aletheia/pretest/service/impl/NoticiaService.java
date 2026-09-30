package com.example.aletheia.pretest.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.aletheia.pretest.dto.NoticiaCreateDTO;
import com.example.aletheia.pretest.dto.NoticiaFeedDTO;
import com.example.aletheia.pretest.dto.NoticiaResponseDTO;
import com.example.aletheia.pretest.exception.ModelNotFoundException;
import com.example.aletheia.pretest.mapper.NoticiaMapper;
import com.example.aletheia.pretest.models.CategoriaVeredicto;
import com.example.aletheia.pretest.models.Noticia;
import com.example.aletheia.pretest.models.enums.Forma;
import com.example.aletheia.pretest.repository.CategoriaVeredictoRepo;
import com.example.aletheia.pretest.repository.NoticiaRepo;
import com.example.aletheia.pretest.service.INoticiaService;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class NoticiaService extends GenericService<Noticia, Integer> implements INoticiaService {
    private final NoticiaRepo repo;
    private final CategoriaVeredictoRepo categoriaVeredictoRepo;
    private final NoticiaMapper mapper;

    @Override 
    protected NoticiaRepo getRepo() {
        return repo;
    }

    @Override 
    public NoticiaResponseDTO createNoticia(NoticiaCreateDTO noticiaCreateDTO){
        Noticia noticia = mapper.toEntity(noticiaCreateDTO);
         CategoriaVeredicto categoria = categoriaVeredictoRepo.findById(noticiaCreateDTO.getCategoriaVeredictoId())
                .orElseThrow(() -> new ModelNotFoundException("Categoría no encontrada"));
        noticia.setCategoriaVeredicto(categoria);
        return mapper.toResponseDTO(repo.save(noticia));
    }

    

    @Override
    public List<NoticiaFeedDTO> obtenerFeedPorForma(Forma forma) {
        return mapper.toFeedDTOList(repo.findByForma(forma));
    }

    @Override
    public List<NoticiaResponseDTO> listarTodas() {
        return mapper.toResponseDTOList(repo.findAll());
    }
}
