package com.example.aletheia.pretest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class NoticiaFeedDTO {
    private Integer id;
    private String titular;
    private String epigrafe;
    private String subtitulo;
    private String entradilla;
    private String cuerpo;
    private String cierre;
    private String img;
    private String fuente;
}