package com.example.aletheia.pretest.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.aletheia.pretest.models.Noticia;
import com.example.aletheia.pretest.models.enums.Forma;

public interface NoticiaRepo extends GenericRepo<Noticia, Integer> {
    List<Noticia> findByForma(Forma forma);

    Page<Noticia> findByForma(Forma forma, Pageable pageable);
}
