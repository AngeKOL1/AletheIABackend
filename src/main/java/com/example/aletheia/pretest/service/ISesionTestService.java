package com.example.aletheia.pretest.service;

import com.example.aletheia.pretest.models.SesionTest;
import com.example.aletheia.pretest.models.enums.Fase;

public interface ISesionTestService extends IGenericService<SesionTest, Integer> {
    SesionTest iniciar(Integer participanteId, Fase fase, String titulo);

    SesionTest finalizar(Integer sesionId);
}