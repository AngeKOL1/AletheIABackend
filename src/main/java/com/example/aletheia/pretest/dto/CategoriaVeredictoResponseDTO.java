package com.example.aletheia.pretest.dto;

import com.example.aletheia.pretest.models.enums.NombreCategoria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class CategoriaVeredictoResponseDTO {
    private Integer id;
    private NombreCategoria nombre;
}
