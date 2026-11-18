package com.example.aletheia.pretest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.aletheia.pretest.dto.PrediccionManualResponseDTO;
import com.example.aletheia.pretest.models.PrediccionManual;
import com.example.aletheia.pretest.models.enums.Fase;

public interface PrediccionManualRepo extends GenericRepo<PrediccionManual, Integer> {

    /** Respuestas del participante en una fase, en una sola consulta (sin N+1 por la categoría). */
    @Query("""
            SELECT new com.example.aletheia.pretest.dto.PrediccionManualResponseDTO(
                pm.id, pm.noticia.id, c.nombre, pm.fase, pm.ordenPresentacion,
                pm.tiempoRespuestaSeg, pm.confianzaAutopercibida, pm.fechaHora)
            FROM PrediccionManual pm JOIN pm.categoriaElegida c
            WHERE pm.participante.id = :participanteId AND pm.fase = :fase
            ORDER BY pm.ordenPresentacion, pm.id
            """)
    List<PrediccionManualResponseDTO> findRespuestasByParticipanteIdAndFase(
            @Param("participanteId") Integer participanteId, @Param("fase") Fase fase);

    long countByParticipanteIdAndFase(Integer participanteId, Fase fase);

    boolean existsByParticipanteIdAndNoticiaIdAndFase(Integer participanteId, Integer noticiaId, Fase fase);

    @Query("SELECT pm.noticia.id FROM PrediccionManual pm WHERE pm.participante.id = :participanteId AND pm.fase = :fase")
    List<Integer> findNoticiaIdsByParticipanteIdAndFase(@Param("participanteId") Integer participanteId,
            @Param("fase") Fase fase);
}
