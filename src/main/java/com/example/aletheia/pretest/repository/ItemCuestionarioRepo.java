package com.example.aletheia.pretest.repository;

import java.util.List;

import com.example.aletheia.pretest.models.ItemCuestionario;

public interface ItemCuestionarioRepo extends GenericRepo<ItemCuestionario, Integer> {
    List<ItemCuestionario> findAllByOrderByOrdenAsc();
}