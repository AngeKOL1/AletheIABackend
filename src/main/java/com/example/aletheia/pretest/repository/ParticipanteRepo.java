package com.example.aletheia.pretest.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.aletheia.pretest.dto.ParticipanteResponseDTO;
import com.example.aletheia.pretest.models.Participante;
import com.example.aletheia.pretest.models.enums.Forma;

public interface ParticipanteRepo extends GenericRepo<Participante, Integer> {

    Optional<Participante> findByCorreoIgnoreCase(String correo);

    boolean existsByCorreoIgnoreCase(String correo);

    /** Solo la forma asignada; vacío si el participante no tiene forma (p. ej. investigadores). */
    @Query("SELECT p.formaAsignada FROM Participante p WHERE p.id = :id")
    Optional<Forma> findFormaAsignadaById(@Param("id") Integer id);

    /** Listado paginado sin cargar el hash de la contraseña ni otros campos internos. */
    @Query(value = """
            SELECT new com.example.aletheia.pretest.dto.ParticipanteResponseDTO(
                p.id, p.correo, p.nombres, p.apellidos, p.edad,
                p.genero, p.nivelEducativo, p.frecuenciaConsumoNoticias)
            FROM Participante p
            """,
            countQuery = "SELECT count(p) FROM Participante p")
    Page<ParticipanteResponseDTO> findAllResumen(Pageable pageable);
}
