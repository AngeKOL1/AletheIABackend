package com.example.aletheia.pretest.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.aletheia.pretest.dto.*;
import com.example.aletheia.pretest.models.Noticia;

@Mapper(componentModel = "spring")
public interface NoticiaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoriaVeredicto", ignore = true)
    @Mapping(target = "textoOcrBruto", ignore = true)
    Noticia toEntity(NoticiaCreateDTO dto);

    @Mapping(target = "categoriaVeredictoNombre", source = "categoriaVeredicto.nombre")
    NoticiaResponseDTO toResponseDTO(Noticia entity);

    NoticiaFeedDTO toFeedDTO(Noticia entity);

    List<NoticiaResponseDTO> toResponseDTOList(List<Noticia> entities);

    List<NoticiaFeedDTO> toFeedDTOList(List<Noticia> entities);
}