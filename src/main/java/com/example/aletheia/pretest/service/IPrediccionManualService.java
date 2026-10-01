package com.example.aletheia.pretest.service;

import java.util.List;

import com.example.aletheia.pretest.dto.PrediccionManualCreateDTO;
import com.example.aletheia.pretest.dto.PrediccionManualResponseDTO;
import com.example.aletheia.pretest.models.PrediccionManual;
import com.example.aletheia.pretest.models.enums.Fase;

public interface IPrediccionManualService extends IGenericService<PrediccionManual, Integer> {
    PrediccionManualResponseDTO registrar(Integer participanteId, PrediccionManualCreateDTO dto);

    List<PrediccionManualResponseDTO> listarPorParticipanteYFase(Integer participanteId, Fase fase);

    long contarRespondidas(Integer participanteId, Fase fase);

    List<Integer> obtenerIdsNoticiasRespondidas(Integer participanteId, Fase fase);
}