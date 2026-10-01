package com.example.aletheia.pretest.service.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.aletheia.pretest.dto.EstadoParticipanteDTO;
import com.example.aletheia.pretest.dto.PageResponseDTO;
import com.example.aletheia.pretest.dto.ParticipanteRegisterDTO;
import com.example.aletheia.pretest.dto.ParticipanteResponseDTO;
import com.example.aletheia.pretest.exception.ModelNotFoundException;
import com.example.aletheia.pretest.mapper.ParticipanteMapper;
import com.example.aletheia.pretest.models.Participante;
import com.example.aletheia.pretest.models.SesionTest;
import com.example.aletheia.pretest.models.enums.Fase;
import com.example.aletheia.pretest.models.enums.Forma;
import com.example.aletheia.pretest.models.enums.Rol;
import com.example.aletheia.pretest.repository.NoticiaRepo;
import com.example.aletheia.pretest.repository.ParticipanteRepo;
import com.example.aletheia.pretest.repository.PrediccionManualRepo;
import com.example.aletheia.pretest.repository.SesionTestRepo;
import com.example.aletheia.pretest.service.IParticipanteService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParticipanteService extends GenericService<Participante, Integer> implements IParticipanteService {

    private final ParticipanteRepo repo;
    private final NoticiaRepo noticiaRepo;
    private final PrediccionManualRepo prediccionManualRepo;
    private final SesionTestRepo sesionTestRepo;
    private final ParticipanteMapper mapper;
    private final PasswordEncoder passwordEncoderService;

    @Override
    protected ParticipanteRepo getRepo() {
        return repo;
    }

    @Override
    public ParticipanteResponseDTO registrar(ParticipanteRegisterDTO dto) {
        Participante participante = mapper.toEntity(dto);
        participante.setContrasenaHash(passwordEncoderService.encode(dto.getContrasena()));
        participante.setRol(Rol.PARTICIPANTE);
        participante.setFormaAsignada(new Random().nextBoolean() ? Forma.A : Forma.B);
        Participante savedParticipante = repo.save(participante);
        return mapper.toResponseDTO(savedParticipante);
    }

    @Override
    public PageResponseDTO<ParticipanteResponseDTO> listarParticipantes(Pageable pageable) {
        Page<Participante> pagina = repo.findAll(pageable);
        Page<ParticipanteResponseDTO> paginaDTO = pagina.map(mapper::toResponseDTO);
        return PageResponseDTO.from(paginaDTO);
    }

    @Override
    public EstadoParticipanteDTO obtenerEstado(Integer participanteId, Fase fase) {
        Participante participante = repo.findById(participanteId)
                .orElseThrow(() -> new ModelNotFoundException("Participante no encontrado"));

        long respondidas = prediccionManualRepo.countByParticipanteIdAndFase(participanteId, fase);
        long totalNoticias = noticiaRepo.countByForma(participante.getFormaAsignada());

        Optional<SesionTest> sesion = sesionTestRepo.findByParticipanteIdAndFase(participanteId, fase);
        boolean cuestionarioCompletado = sesion.map(s -> s.getHoraFin() != null).orElse(false);
        Integer sesionAbiertaId = sesion.filter(s -> s.getHoraFin() == null).map(SesionTest::getId).orElse(null);

        return new EstadoParticipanteDTO(respondidas, totalNoticias, cuestionarioCompletado, sesionAbiertaId);
    }

}
