package com.example.aletheia.pretest.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.aletheia.pretest.dto.ItemCuestionarioResponseDTO;
import com.example.aletheia.pretest.mapper.ItemCuestionarioMapper;
import com.example.aletheia.pretest.models.ItemCuestionario;
import com.example.aletheia.pretest.models.enums.Fase;
import com.example.aletheia.pretest.models.enums.FaseAplicacion;
import com.example.aletheia.pretest.repository.ItemCuestionarioRepo;
import com.example.aletheia.pretest.service.IItemCuestionarioService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ItemCuestionarioService extends GenericService<ItemCuestionario, Integer>
        implements IItemCuestionarioService {
    private final ItemCuestionarioRepo repo;
    private final ItemCuestionarioMapper mapper;

    @Override
    protected ItemCuestionarioRepo getRepo() {
        return repo;
    }

    @Override
    public List<ItemCuestionarioResponseDTO> listar(Fase fase) {
        List<FaseAplicacion> fasesPermitidas = (fase == Fase.PRE)
                ? List.of(FaseAplicacion.PRE_POST)
                : List.of(FaseAplicacion.PRE_POST, FaseAplicacion.SOLO_POST);

        return mapper.toResponseDTOList(repo.findByFaseAplicacionInOrderByOrdenAsc(fasesPermitidas));
    }
}
