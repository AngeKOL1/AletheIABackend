package com.example.aletheia.pretest.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.aletheia.pretest.dto.RespuestaCuestionarioLoteDTO;
import com.example.aletheia.pretest.exception.ModelNotFoundException;
import com.example.aletheia.pretest.models.ItemCuestionario;
import com.example.aletheia.pretest.models.RespuestaCuestionario;
import com.example.aletheia.pretest.models.SesionTest;
import com.example.aletheia.pretest.models.enums.Fase;
import com.example.aletheia.pretest.models.enums.FaseAplicacion;
import com.example.aletheia.pretest.repository.ItemCuestionarioRepo;
import com.example.aletheia.pretest.repository.RespuestaCuestionarioRepo;
import com.example.aletheia.pretest.repository.SesionTestRepo;
import com.example.aletheia.pretest.service.IRespuestaCuestionarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RespuestaCuestionarioService extends GenericService<RespuestaCuestionario, Integer> implements IRespuestaCuestionarioService {

    private final RespuestaCuestionarioRepo repo;
    private final SesionTestRepo sesionTestRepo;
    private final ItemCuestionarioRepo itemRepo;

    @Override
    protected RespuestaCuestionarioRepo getRepo(){
        return repo;
    }

    @Override
    @Transactional
    public void registrarLote(Integer participanteId, RespuestaCuestionarioLoteDTO dto) {
        // Solo se encuentra si la sesión pertenece al participante del token.
        SesionTest sesion = sesionTestRepo.findByIdAndParticipanteId(dto.getSesionTestId(), participanteId)
                .orElseThrow(() -> new ModelNotFoundException("Sesión no encontrada"));

        if (sesion.getHoraFin() != null) {
            throw new IllegalStateException("La sesión ya fue finalizada; no se aceptan más respuestas");
        }

        List<RespuestaCuestionarioLoteDTO.ItemRespuesta> entrada = dto.getRespuestas();

        Set<Integer> idsVistos = new HashSet<>();
        for (RespuestaCuestionarioLoteDTO.ItemRespuesta r : entrada) {
            if (!idsVistos.add(r.getItemCuestionarioId())) {
                throw new IllegalArgumentException("El ítem " + r.getItemCuestionarioId() + " está repetido en el lote");
            }
        }

        Map<Integer, ItemCuestionario> items = itemRepo.findAllById(idsVistos).stream()
                .collect(Collectors.toMap(ItemCuestionario::getId, Function.identity()));

        List<RespuestaCuestionario> respuestas = entrada.stream().map(r -> {
            ItemCuestionario item = items.get(r.getItemCuestionarioId());
            if (item == null) {
                throw new ModelNotFoundException("Ítem no encontrado: " + r.getItemCuestionarioId());
            }
            if (!aplicaEnFase(item.getFaseAplicacion(), sesion.getFase())) {
                throw new IllegalArgumentException(
                        "El ítem " + item.getId() + " no corresponde a la fase " + sesion.getFase());
            }
            if (!item.getTipoRespuesta().esRespuestaValida(r.getRespuesta())) {
                throw new IllegalArgumentException(
                        "Respuesta inválida para el ítem " + item.getId() + " (tipo " + item.getTipoRespuesta().getEtiqueta() + ")");
            }
            if (repo.existsBySesionTestIdAndItemCuestionarioId(sesion.getId(), item.getId())) {
                throw new IllegalStateException("El ítem " + item.getId() + " ya fue respondido en esta sesión");
            }

            RespuestaCuestionario entity = new RespuestaCuestionario();
            entity.setSesionTest(sesion);
            entity.setItemCuestionario(item);
            entity.setRespuesta(r.getRespuesta().trim());
            return entity;
        }).toList();

        repo.saveAll(respuestas);
    }

    private boolean aplicaEnFase(FaseAplicacion faseAplicacion, Fase fase) {
        return faseAplicacion == FaseAplicacion.PRE_POST || fase == Fase.POST;
    }
}
