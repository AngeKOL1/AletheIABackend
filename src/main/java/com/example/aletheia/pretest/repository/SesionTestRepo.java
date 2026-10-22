package com.example.aletheia.pretest.repository;

import java.util.Optional;

import com.example.aletheia.pretest.models.SesionTest;
import com.example.aletheia.pretest.models.enums.Fase;

public interface SesionTestRepo extends GenericRepo<SesionTest, Integer> {
    Optional<SesionTest> findByParticipanteIdAndFase(Integer participanteId, Fase fase);

    Optional<SesionTest> findByIdAndParticipanteId(Integer id, Integer participanteId);
}
