package com.example.aletheia.pretest.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.example.aletheia.pretest.dto.ItemCuestionarioResponseDTO;
import com.example.aletheia.pretest.models.ItemCuestionario;

@Mapper(componentModel = "spring")
public interface ItemCuestionarioMapper {
    ItemCuestionarioResponseDTO toResponseDTO(ItemCuestionario entity);

    List<ItemCuestionarioResponseDTO> toResponseDTOList(List<ItemCuestionario> entities);
}