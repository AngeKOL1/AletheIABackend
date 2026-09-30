package com.example.aletheia.pretest.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data 
public class NoticiaResponseDTO {
    private Integer id;
    private String titular;
    private String epigrafe;
    private String subtitulo;
    private String entradilla;
    private String cuerpo;
    private String cierre;
    private String img;
    private String fuenteTexto;
    private String tema;
    private String forma;
    private LocalDateTime fechaPublicacion;
}
