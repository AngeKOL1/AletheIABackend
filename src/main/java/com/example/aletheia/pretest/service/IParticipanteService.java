package com.example.aletheia.pretest.service;

import com.example.aletheia.pretest.dto.ParticipanteRegisterDTO;
import com.example.aletheia.pretest.dto.ParticipanteResponseDTO;
import com.example.aletheia.pretest.models.Participante;

public interface IParticipanteService extends IGenericService<Participante, Integer> {
    ParticipanteResponseDTO registrar(ParticipanteRegisterDTO dto);
}
