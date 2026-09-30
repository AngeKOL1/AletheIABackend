package com.example.aletheia.pretest.repository;

import com.example.aletheia.pretest.models.CategoriaVeredicto;
import com.example.aletheia.pretest.models.enums.NombreCategoria;

public interface CategoriaVeredictoRepo extends GenericRepo<CategoriaVeredicto, Integer> {
    boolean existsByNombre(NombreCategoria nombre);
}
