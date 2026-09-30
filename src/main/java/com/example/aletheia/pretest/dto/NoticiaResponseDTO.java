package com.example.aletheia.pretest.dto;

import com.example.aletheia.pretest.models.enums.Forma;
import com.example.aletheia.pretest.models.enums.NombreCategoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
public class NoticiaResponseDTO {
    private Integer id;
    private String titular;
    private String img;
    private String fuente;
    private String tema;
    private Forma forma;
    private NombreCategoria categoriaVeredictoNombre;
}