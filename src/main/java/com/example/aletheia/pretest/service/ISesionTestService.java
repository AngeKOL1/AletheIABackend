package com.example.aletheia.pretest.service;

import com.example.aletheia.pretest.dto.SesionTestResponseDTO;
import com.example.aletheia.pretest.models.SesionTest;
import com.example.aletheia.pretest.models.enums.Fase;

public interface ISesionTestService extends IGenericService<SesionTest, Integer> {
    SesionTestResponseDTO iniciar(Integer participanteId, Fase fase, String titulo);

    SesionTestResponseDTO finalizar(Integer participanteId, Integer sesionId);
}
