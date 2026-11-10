package com.example.aletheia.pretest.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.aletheia.pretest.models.RespuestaCuestionario;

public interface RespuestaCuestionarioRepo extends GenericRepo<RespuestaCuestionario, Integer> {

    /** Ids de los ítems ya respondidos en la sesión (una consulta para todo el lote). */
    @Query("SELECT r.itemCuestionario.id FROM RespuestaCuestionario r WHERE r.sesionTest.id = :sesionId")
    Set<Integer> findItemIdsRespondidosBySesionTestId(@Param("sesionId") Integer sesionId);
}
