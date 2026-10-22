package com.example.aletheia.pretest.dto;

import com.example.aletheia.pretest.models.enums.Dimension;
import com.example.aletheia.pretest.models.enums.FaseAplicacion;
import com.example.aletheia.pretest.models.enums.TipoRespuesta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCuestionarioResponseDTO {
    private Integer id;
    private String contenido;
    private TipoRespuesta tipoRespuesta; // se serializa como "likert_5" / "texto"
    private Integer orden;
    private Dimension dimension;
    private FaseAplicacion faseAplicacion;
}
