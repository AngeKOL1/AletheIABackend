package com.example.aletheia.pretest.mapper;

import org.mapstruct.Mapper;

import com.example.aletheia.pretest.dto.SesionTestResponseDTO;
import com.example.aletheia.pretest.models.SesionTest;

@Mapper(componentModel = "spring")
public interface SesionTestMapper {
    SesionTestResponseDTO toResponseDTO(SesionTest entity);
}
