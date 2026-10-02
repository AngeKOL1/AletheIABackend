package com.example.aletheia.pretest.dto;

import com.example.aletheia.pretest.models.enums.FaseAplicacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCuestionarioResponseDTO {
    private Integer id;
    private String contenido;
    private String tipoRespuesta;
    private Integer orden;
    private String dimension;
    private FaseAplicacion faseAplicacion;
}