package com.example.aletheia.pretest.service;

import java.util.List;

import com.example.aletheia.pretest.dto.NoticiaCreateDTO;
import com.example.aletheia.pretest.dto.NoticiaFeedDTO;
import com.example.aletheia.pretest.dto.NoticiaResponseDTO;
import com.example.aletheia.pretest.models.Noticia;
import com.example.aletheia.pretest.models.enums.Forma;

public interface INoticiaService extends IGenericService<Noticia, Integer> {
    NoticiaResponseDTO createNoticia(NoticiaCreateDTO noticiaCreateDTO);

    List<NoticiaFeedDTO> obtenerFeedPorForma(Forma forma);

    List<NoticiaResponseDTO> listarTodas();

    List<NoticiaFeedDTO> obtenerFeedParaParticipante(Integer participanteId);
}
