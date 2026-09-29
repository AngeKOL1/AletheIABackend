package com.example.aletheia.pretest.models;

import com.example.aletheia.pretest.models.enums.NombreCategoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table(name = "categoria_veredicto", uniqueConstraints = {
        @UniqueConstraint (columnNames = "nombre")
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoriaVeredicto {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(name ="nombre", nullable = false)
    private NombreCategoria nombre;
}
