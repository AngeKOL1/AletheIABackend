package com.example.aletheia.pretest.service;

import com.example.aletheia.pretest.dto.NoticiaCreateDTO;
import com.example.aletheia.pretest.dto.NoticiaResponseDTO;
import com.example.aletheia.pretest.models.Noticia;

public interface INoticiaService extends IGenericService<Noticia, Integer> {
    NoticiaResponseDTO createNoticia(NoticiaCreateDTO noticiaCreateDTO);
}
