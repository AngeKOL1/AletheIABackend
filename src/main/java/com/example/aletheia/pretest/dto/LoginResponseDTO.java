package com.example.aletheia.pretest.dto;

import com.example.aletheia.pretest.models.enums.Rol;
import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
public class LoginResponseDTO {
    private String token;
    private Integer id;
    private String nombres;
    private String correo;
    private Rol rol;
}
