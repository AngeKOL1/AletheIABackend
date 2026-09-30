package com.example.aletheia.pretest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.aletheia.pretest.dto.ParticipanteRegisterDTO;
import com.example.aletheia.pretest.dto.ParticipanteResponseDTO;
import com.example.aletheia.pretest.models.Participante;

@Mapper(componentModel = "spring")
public interface ParticipanteMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contrasenaHash", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    Participante toEntity(ParticipanteRegisterDTO dto);

    ParticipanteResponseDTO toResponseDTO(Participante entity);
}
