package com.example.aletheia.pretest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.aletheia.pretest.dto.*;
import com.example.aletheia.pretest.models.Noticia;

// Los listados (feed y resumen) se arman directamente en NoticiaRepo con consultas a DTO.
@Mapper(componentModel = "spring")
public interface NoticiaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoriaVeredicto", ignore = true)
    @Mapping(target = "textoOcrBruto", ignore = true)
    Noticia toEntity(NoticiaCreateDTO dto);

    @Mapping(target = "categoriaVeredictoNombre", source = "categoriaVeredicto.nombre")
    NoticiaResponseDTO toResponseDTO(Noticia entity);
}
