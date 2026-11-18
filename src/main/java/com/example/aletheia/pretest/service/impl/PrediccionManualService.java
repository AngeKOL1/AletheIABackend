package com.example.aletheia.pretest.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.aletheia.pretest.dto.PrediccionManualCreateDTO;
import com.example.aletheia.pretest.dto.PrediccionManualResponseDTO;
import com.example.aletheia.pretest.exception.ModelNotFoundException;
import com.example.aletheia.pretest.mapper.PrediccionManualMapper;
import com.example.aletheia.pretest.models.CategoriaVeredicto;
import com.example.aletheia.pretest.models.PrediccionManual;
import com.example.aletheia.pretest.models.enums.Fase;
import com.example.aletheia.pretest.models.enums.Forma;
import com.example.aletheia.pretest.repository.CategoriaVeredictoRepo;
import com.example.aletheia.pretest.repository.NoticiaRepo;
import com.example.aletheia.pretest.repository.ParticipanteRepo;
import com.example.aletheia.pretest.repository.PrediccionManualRepo;
import com.example.aletheia.pretest.service.IPrediccionManualService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PrediccionManualService extends GenericService<PrediccionManual, Integer>
        implements IPrediccionManualService {
    protected final PrediccionManualRepo repo;
    private final ParticipanteRepo participanteRepo;
    private final NoticiaRepo noticiaRepo;
    private final CategoriaVeredictoRepo categoriaRepo;
    private final PrediccionManualMapper mapper;

    @Override
    protected PrediccionManualRepo getRepo() {
        return repo;
    }

    @Override
    @Transactional
    public PrediccionManualResponseDTO registrar(Integer participanteId, PrediccionManualCreateDTO dto) {
        if (repo.existsByParticipanteIdAndNoticiaIdAndFase(participanteId, dto.getNoticiaId(), dto.getFase())) {
            throw new IllegalStateException("Ya existe una respuesta para esta noticia en esta fase");
        }

        // Solo se leen las formas; no se cargan la noticia (con su cuerpo) ni el participante completos.
        Forma formaNoticia = noticiaRepo.findFormaById(dto.getNoticiaId())
                .orElseThrow(() -> new ModelNotFoundException("Noticia no encontrada"));
        Forma formaParticipante = participanteRepo.findFormaAsignadaById(participanteId).orElse(null);
        if (formaNoticia != formaParticipante) {
            throw new IllegalArgumentException("La noticia no pertenece a la forma asignada al participante");
        }
        CategoriaVeredicto categoria = categoriaRepo.findById(dto.getCategoriaElegidaId())
                .orElseThrow(() -> new ModelNotFoundException("Categoría no encontrada"));

        PrediccionManual entity = new PrediccionManual();
        // Referencias: solo aportan la clave foránea, sin consultar la BD.
        entity.setParticipante(participanteRepo.getReferenceById(participanteId));
        entity.setNoticia(noticiaRepo.getReferenceById(dto.getNoticiaId()));
        entity.setCategoriaElegida(categoria);
        entity.setFase(dto.getFase());
        entity.setOrdenPresentacion(dto.getOrdenPresentacion());
        entity.setTiempoRespuestaSeg(dto.getTiempoRespuestaSeg());
        entity.setConfianzaAutopercibida(dto.getConfianzaAutopercibida());

        return mapper.toResponseDTO(repo.save(entity));
    }

    @Override
    public List<PrediccionManualResponseDTO> listarPorParticipanteYFase(Integer participanteId, Fase fase) {
        return repo.findRespuestasByParticipanteIdAndFase(participanteId, fase);
    }

    @Override
    public long contarRespondidas(Integer participanteId, Fase fase) {
        return repo.countByParticipanteIdAndFase(participanteId, fase);
    }

    @Override
    public List<Integer> obtenerIdsNoticiasRespondidas(Integer participanteId, Fase fase) {
        return repo.findNoticiaIdsByParticipanteIdAndFase(participanteId, fase);
    }

}
