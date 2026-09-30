package com.example.aletheia.pretest.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.aletheia.pretest.models.enums.Forma;
import com.example.aletheia.pretest.models.enums.Rol;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participante", uniqueConstraints = {
        @UniqueConstraint(columnNames = "correo")
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank
    @Column(name = "nombres", nullable = false)
    private String nombres;

    @NotBlank
    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @NotBlank
    @Email
    @Column(name = "correo", nullable = false)
    private String correo;

    @NotBlank
    @JsonIgnore
    @Column(name = "contrasena_hash", nullable = false)
    private String contrasenaHash;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "genero")
    private String genero;

    @Column(name = "nivel_educativo")
    private String nivelEducativo;

    @Column(name = "frecuencia_consumo_noticias")
    private String frecuenciaConsumoNoticias;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_asignada")
    private Forma formaAsignada;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;

    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
}