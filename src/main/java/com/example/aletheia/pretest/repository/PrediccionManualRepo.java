package com.example.aletheia.pretest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.aletheia.pretest.models.PrediccionManual;
import com.example.aletheia.pretest.models.enums.Fase;

public interface PrediccionManualRepo extends GenericRepo<PrediccionManual, Integer> {
    List<PrediccionManual> findByParticipanteIdAndFase(Integer participanteId, Fase fase);

    List<PrediccionManual> findByParticipanteIdOrderByOrdenPresentacionAsc(Integer participanteId);

    long countByParticipanteIdAndFase(Integer participanteId, Fase fase);

    boolean existsByParticipanteIdAndNoticiaIdAndFase(Integer participanteId, Integer noticiaId, Fase fase);

    @Query("SELECT pm.noticia.id FROM PrediccionManual pm WHERE pm.participante.id = :participanteId AND pm.fase = :fase")
    List<Integer> findNoticiaIdsByParticipanteIdAndFase(@Param("participanteId") Integer participanteId,
            @Param("fase") Fase fase);
}
