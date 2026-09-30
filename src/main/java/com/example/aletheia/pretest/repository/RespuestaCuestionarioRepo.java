package com.example.aletheia.pretest.repository;

import java.util.List;

import com.example.aletheia.pretest.models.RespuestaCuestionario;

public interface RespuestaCuestionarioRepo extends GenericRepo<RespuestaCuestionario, Integer> {
    List<RespuestaCuestionario> findBySesionTestId(Integer sesionTestId);

    boolean existsBySesionTestIdAndItemCuestionarioId(Integer sesionId, Integer itemId);
}
