package com.example.aletheia.pretest.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.aletheia.pretest.dto.PageResponseDTO;
import com.example.aletheia.pretest.dto.ParticipanteRegisterDTO;
import com.example.aletheia.pretest.dto.ParticipanteResponseDTO;
import com.example.aletheia.pretest.mapper.ParticipanteMapper;
import com.example.aletheia.pretest.models.Participante;
import com.example.aletheia.pretest.repository.ParticipanteRepo;
import com.example.aletheia.pretest.service.IParticipanteService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParticipanteService extends GenericService<Participante, Integer> implements IParticipanteService {

    private final ParticipanteRepo repo;
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
        Participante savedParticipante = repo.save(participante);
        return mapper.toResponseDTO(savedParticipante);
    }

    @Override
    public PageResponseDTO<ParticipanteResponseDTO> listarParticipantes(Pageable pageable) {
        Page<Participante> pagina = repo.findAll(pageable);
        Page<ParticipanteResponseDTO> paginaDTO = pagina.map(mapper::toResponseDTO);
        return PageResponseDTO.from(paginaDTO);
    }

}
