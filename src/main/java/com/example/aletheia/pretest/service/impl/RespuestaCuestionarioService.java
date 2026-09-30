package com.example.aletheia.pretest.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.aletheia.pretest.dto.RespuestaCuestionarioLoteDTO;
import com.example.aletheia.pretest.exception.ModelNotFoundException;
import com.example.aletheia.pretest.models.ItemCuestionario;
import com.example.aletheia.pretest.models.RespuestaCuestionario;
import com.example.aletheia.pretest.models.SesionTest;
import com.example.aletheia.pretest.repository.ItemCuestionarioRepo;
import com.example.aletheia.pretest.repository.RespuestaCuestionarioRepo;
import com.example.aletheia.pretest.repository.SesionTestRepo;
import com.example.aletheia.pretest.service.IRespuestaCuestionarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RespuestaCuestionarioService extends GenericService<RespuestaCuestionario, Integer> implements IRespuestaCuestionarioService {

    private final RespuestaCuestionarioRepo repo;
    private final SesionTestRepo sesionTestRepo;
    private final ItemCuestionarioRepo itemRepo;

    @Override 
    protected RespuestaCuestionarioRepo getRepo(){
        return repo;
    }

    @Override 
    public void registrarLote(RespuestaCuestionarioLoteDTO dto) {
        SesionTest sesion = sesionTestRepo.findById(dto.getSesionTestId())
                .orElseThrow(() -> new ModelNotFoundException("Sesión no encontrada"));

        List<RespuestaCuestionario> respuestas = dto.getRespuestas().stream().map(r -> {
            ItemCuestionario item = itemRepo.findById(r.getItemCuestionarioId())
                    .orElseThrow(() -> new ModelNotFoundException("Ítem no encontrado"));
            RespuestaCuestionario entity = new RespuestaCuestionario();
            entity.setSesionTest(sesion);
            entity.setItemCuestionario(item);
            entity.setRespuesta(r.getRespuesta());
            return entity;
        }).toList();

        repo.saveAll(respuestas);
    }
}