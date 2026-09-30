package com.example.aletheia.pretest.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.example.aletheia.pretest.dto.CategoriaVeredictoResponseDTO;
import com.example.aletheia.pretest.models.CategoriaVeredicto;

@Mapper(componentModel = "spring")
public interface CategoriaVeredictoMapper {

    CategoriaVeredictoResponseDTO toResponseDTO(CategoriaVeredicto entity);

    List<CategoriaVeredictoResponseDTO> toResponseDTOList(List<CategoriaVeredicto> entities);
}
