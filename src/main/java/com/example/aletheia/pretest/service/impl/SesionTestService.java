package com.example.aletheia.pretest.service.impl;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.example.aletheia.pretest.models.Participante;
import com.example.aletheia.pretest.models.SesionTest;
import com.example.aletheia.pretest.models.enums.Fase;
import com.example.aletheia.pretest.repository.ParticipanteRepo;
import com.example.aletheia.pretest.repository.SesionTestRepo;
import com.example.aletheia.pretest.service.ISesionTestService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SesionTestService extends GenericService<SesionTest, Integer> implements ISesionTestService {

    private final SesionTestRepo repo;
    private final ParticipanteRepo participanteRepo;

    @Override 
    protected SesionTestRepo getRepo(){
        return repo;
    }

    @Override 
    public SesionTest iniciar(Integer participanteId, Fase fase, String titulo) {
        Participante participante = participanteRepo.findById(participanteId)
                .orElseThrow(() -> new EntityNotFoundException("Participante no encontrado"));
        SesionTest sesion = new SesionTest();
        sesion.setParticipante(participante);
        sesion.setFase(fase);
        sesion.setTitulo(titulo);
        sesion.setHoraInicio(LocalDateTime.now());
        return repo.save(sesion);
    }

    @Override 
    public SesionTest finalizar(Integer sesionId) {
        SesionTest sesion = repo.findById(sesionId)
                .orElseThrow(() -> new EntityNotFoundException("Sesión no encontrada"));
        sesion.setHoraFin(LocalDateTime.now());
        return repo.save(sesion);
    }
}