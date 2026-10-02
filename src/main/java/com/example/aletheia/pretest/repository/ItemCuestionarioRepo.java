package com.example.aletheia.pretest.repository;

import java.util.List;

import com.example.aletheia.pretest.models.ItemCuestionario;
import com.example.aletheia.pretest.models.enums.FaseAplicacion;

public interface ItemCuestionarioRepo extends GenericRepo<ItemCuestionario, Integer> {
    List<ItemCuestionario> findAllByOrderByOrdenAsc();

    List<ItemCuestionario> findByFaseAplicacionInOrderByOrdenAsc(List<FaseAplicacion> fases);
}