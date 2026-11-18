package com.example.aletheia.pretest.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.aletheia.pretest.dto.NoticiaCreateDTO;
import com.example.aletheia.pretest.dto.NoticiaFeedDTO;
import com.example.aletheia.pretest.dto.NoticiaResponseDTO;
import com.example.aletheia.pretest.exception.ModelNotFoundException;
import com.example.aletheia.pretest.mapper.NoticiaMapper;
import com.example.aletheia.pretest.models.CategoriaVeredicto;
import com.example.aletheia.pretest.models.Noticia;
import com.example.aletheia.pretest.repository.CategoriaVeredictoRepo;
import com.example.aletheia.pretest.repository.NoticiaRepo;
import com.example.aletheia.pretest.repository.ParticipanteRepo;
import com.example.aletheia.pretest.service.INoticiaService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class NoticiaService extends GenericService<Noticia, Integer> implements INoticiaService {
    private final NoticiaRepo repo;
    private final CategoriaVeredictoRepo categoriaVeredictoRepo;
    private final ParticipanteRepo participanteRepo;
    private final NoticiaMapper mapper;

    @Override
    protected NoticiaRepo getRepo() {
        return repo;
    }

    @Override
    @Transactional
    public NoticiaResponseDTO createNoticia(NoticiaCreateDTO noticiaCreateDTO) {
        Noticia noticia = mapper.toEntity(noticiaCreateDTO);
        CategoriaVeredicto categoria = categoriaVeredictoRepo.findById(noticiaCreateDTO.getCategoriaVeredictoId())
                .orElseThrow(() -> new ModelNotFoundException("Categoría no encontrada"));
        noticia.setCategoriaVeredicto(categoria);
        return mapper.toResponseDTO(repo.save(noticia));
    }

    @Override
    public List<NoticiaResponseDTO> listarTodas() {
        return repo.findAllResumen();
    }

    @Override
    public List<NoticiaFeedDTO> obtenerFeedParaParticipante(Integer participanteId) {
        // Sin forma asignada (p. ej. un investigador) no hay feed.
        return participanteRepo.findFormaAsignadaById(participanteId)
                .map(repo::findFeedByForma)
                .orElse(List.of());
    }

    // Todo o nada: si una noticia falla, no se guarda ninguna del lote.
    @Override
    @Transactional
    public List<NoticiaResponseDTO> crearEnLote(List<NoticiaCreateDTO> dtos) {
        return dtos.stream()
                .map(this::createNoticia)
                .toList();
    }
}
