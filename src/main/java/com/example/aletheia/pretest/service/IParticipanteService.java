package com.example.aletheia.pretest.service;

import org.springframework.data.domain.Pageable;

import com.example.aletheia.pretest.dto.EstadoParticipanteDTO;
import com.example.aletheia.pretest.dto.PageResponseDTO;
import com.example.aletheia.pretest.dto.ParticipanteRegisterDTO;
import com.example.aletheia.pretest.dto.ParticipanteResponseDTO;
import com.example.aletheia.pretest.models.Participante;
import com.example.aletheia.pretest.models.enums.Fase;

public interface IParticipanteService extends IGenericService<Participante, Integer> {
    ParticipanteResponseDTO registrar(ParticipanteRegisterDTO dto);

    PageResponseDTO<ParticipanteResponseDTO> listarParticipantes(Pageable pageable);

    EstadoParticipanteDTO obtenerEstado(Integer participanteId, Fase fase);
}
