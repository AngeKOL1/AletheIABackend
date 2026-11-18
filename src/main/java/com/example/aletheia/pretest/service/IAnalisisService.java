package com.example.aletheia.pretest.service;

import java.util.List;

import com.example.aletheia.pretest.dto.AnalisisResponseDTO;
import com.example.aletheia.pretest.dto.AnalisisResumenDTO;
import com.example.aletheia.pretest.models.AnalisisNoticia;

public interface IAnalisisService extends IGenericService<AnalisisNoticia, Integer> {

    /**
     * Pide el análisis de una noticia. Si ya hay uno pendiente o completado se reutiliza;
     * si no, se envía a AletheIA. Responde enseguida: el resultado llega después.
     */
    AnalisisResponseDTO solicitar(Integer participanteId, Integer noticiaId);

    /** Último análisis de la noticia que pidió el participante. */
    AnalisisResponseDTO consultar(Integer participanteId, Integer noticiaId);

    List<AnalisisResumenDTO> listarMisAnalisis(Integer participanteId);

    /** Ids de los análisis que siguen esperando a AletheIA. */
    List<Integer> idsPendientes();

    /** Consulta en AletheIA un análisis pendiente y guarda su resultado, su error o su vencimiento. */
    void actualizarEstado(Integer analisisId);
}
