package com.example.aletheia.pretest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.example.aletheia.pretest.models.AnalisisNoticia;

public interface AnalisisNoticiaRepo extends GenericRepo<AnalisisNoticia, Integer> {

    /** Último análisis de la noticia (el único que puede estar vigente). */
    Optional<AnalisisNoticia> findFirstByNoticiaIdOrderByIdDesc(Integer noticiaId);

    @Query("SELECT a.id FROM AnalisisNoticia a "
            + "WHERE a.estado = com.example.aletheia.pretest.models.enums.EstadoAnalisis.PENDIENTE ORDER BY a.id")
    List<Integer> findIdsPendientes();
}
