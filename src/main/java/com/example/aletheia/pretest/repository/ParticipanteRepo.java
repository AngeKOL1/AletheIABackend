package com.example.aletheia.pretest.repository;

import java.util.Optional;

import com.example.aletheia.pretest.models.Participante;

public interface ParticipanteRepo extends GenericRepo<Participante, Integer> {
    Optional<Participante> findByCorreo(String correo);
}
