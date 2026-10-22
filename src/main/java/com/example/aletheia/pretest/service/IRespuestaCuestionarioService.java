package com.example.aletheia.pretest.service;

import com.example.aletheia.pretest.dto.RespuestaCuestionarioLoteDTO;
import com.example.aletheia.pretest.models.RespuestaCuestionario;

public interface IRespuestaCuestionarioService extends IGenericService<RespuestaCuestionario, Integer> {

    void registrarLote(Integer participanteId, RespuestaCuestionarioLoteDTO dto);
}
