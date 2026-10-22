package com.example.aletheia.pretest.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.aletheia.pretest.dto.SesionTestResponseDTO;
import com.example.aletheia.pretest.exception.ModelNotFoundException;
import com.example.aletheia.pretest.mapper.SesionTestMapper;
import com.example.aletheia.pretest.models.Participante;
import com.example.aletheia.pretest.models.SesionTest;
import com.example.aletheia.pretest.models.enums.Fase;
import com.example.aletheia.pretest.repository.ParticipanteRepo;
import com.example.aletheia.pretest.repository.SesionTestRepo;
import com.example.aletheia.pretest.service.ISesionTestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SesionTestService extends GenericService<SesionTest, Integer> implements ISesionTestService {

    private final SesionTestRepo repo;
    private final ParticipanteRepo participanteRepo;
    private final SesionTestMapper mapper;

    @Override
    protected SesionTestRepo getRepo() {
        return repo;
    }

    @Override
    @Transactional
    public SesionTestResponseDTO iniciar(Integer participanteId, Fase fase, String titulo) {
        Optional<SesionTest> existente = repo.findByParticipanteIdAndFase(participanteId, fase);
        if (existente.isPresent() && existente.get().getHoraFin() == null) {
            return mapper.toResponseDTO(existente.get()); // ya hay una sesión abierta, la reutiliza
        }
        if (existente.isPresent()) {
            throw new IllegalStateException("Ya completaste el cuestionario de esta fase");
        }

        Participante participante = participanteRepo.findById(participanteId)
                .orElseThrow(() -> new ModelNotFoundException("Participante no encontrado"));
        SesionTest sesion = new SesionTest();
        sesion.setParticipante(participante);
        sesion.setFase(fase);
        sesion.setTitulo(titulo.trim());
        sesion.setHoraInicio(LocalDateTime.now());
        return mapper.toResponseDTO(repo.save(sesion));
    }

    @Override
    @Transactional
    public SesionTestResponseDTO finalizar(Integer participanteId, Integer sesionId) {
        // Solo se encuentra si la sesión pertenece al participante del token.
        SesionTest sesion = repo.findByIdAndParticipanteId(sesionId, participanteId)
                .orElseThrow(() -> new ModelNotFoundException("Sesión no encontrada"));
        if (sesion.getHoraFin() != null) {
            throw new IllegalStateException("La sesión ya fue finalizada");
        }
        sesion.setHoraFin(LocalDateTime.now());
        return mapper.toResponseDTO(repo.save(sesion));
    }
}
