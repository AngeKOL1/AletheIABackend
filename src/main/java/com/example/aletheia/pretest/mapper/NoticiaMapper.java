package com.example.aletheia.pretest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.aletheia.pretest.dto.NoticiaCreateDTO;
import com.example.aletheia.pretest.dto.NoticiaResponseDTO;
import com.example.aletheia.pretest.models.Noticia;

@Mapper(componentModel = "spring")
public interface NoticiaMapper {

    @Mapping (target = "id", ignore = true)
    Noticia toEntity(NoticiaCreateDTO dto);

    @Mapping (target = "fuenteTexto", source = "fuente")
    @Mapping (target = "forma", source = "forma")
    NoticiaResponseDTO toResponseDTO(Noticia entity);

}
