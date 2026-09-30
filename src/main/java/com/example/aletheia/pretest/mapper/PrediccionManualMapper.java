package com.example.aletheia.pretest.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.aletheia.pretest.dto.PrediccionManualResponseDTO;
import com.example.aletheia.pretest.models.PrediccionManual;

@Mapper(componentModel = "spring")
public interface PrediccionManualMapper {

    @Mapping(target = "noticiaId", source = "noticia.id")
    @Mapping(target = "categoriaElegidaNombre", source = "categoriaElegida.nombre")
    PrediccionManualResponseDTO toResponseDTO(PrediccionManual entity);

    List<PrediccionManualResponseDTO> toResponseDTOList(List<PrediccionManual> entities);
}