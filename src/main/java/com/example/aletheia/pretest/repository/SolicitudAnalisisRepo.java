package com.example.aletheia.pretest.repository;

import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.aletheia.pretest.models.AnalisisNoticia;
import com.example.aletheia.pretest.models.SolicitudAnalisis;

public interface SolicitudAnalisisRepo extends GenericRepo<SolicitudAnalisis, Integer> {

    boolean existsByParticipanteIdAndAnalisisId(Integer participanteId, Integer analisisId);

    /** Análisis más recientes de una noticia que pidió el participante (usar con Limit.of(1)). */
    @Query("""
            SELECT a FROM SolicitudAnalisis s JOIN s.analisis a
            WHERE s.participante.id = :participanteId AND a.noticia.id = :noticiaId
            ORDER BY a.id DESC
            """)
    List<AnalisisNoticia> findAnalisisDeParticipante(@Param("participanteId") Integer participanteId,
            @Param("noticiaId") Integer noticiaId, Limit limit);

    @Query("""
            SELECT a.id AS analisisId, a.noticia.id AS noticiaId, a.estado AS estado,
                   a.veredictoGlobal AS veredictoGlobal, a.motivoSinVeredicto AS motivoSinVeredicto,
                   a.actualizado AS actualizado
            FROM SolicitudAnalisis s JOIN s.analisis a
            WHERE s.participante.id = :participanteId
            ORDER BY a.id DESC
            """)
    List<AnalisisResumenProyeccion> findResumenByParticipanteId(@Param("participanteId") Integer participanteId);
}
