package com.example.aletheia.pretest.dto;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Informe preparado para mostrar. Si {@code veredictoGlobal} es null se muestra
 * {@code motivoSinVeredicto} (que no equivale a «no verificable»).
 */
public record InformeVistaDTO(
        String veredictoGlobal,
        String veredictoGlobalEtiqueta,
        String motivoSinVeredicto,
        String motivoSinVeredictoEtiqueta,
        String justificacionGlobal,
        DistorsionVistaDTO distorsionGlobal,
        List<AfirmacionVistaDTO> afirmaciones,
        List<String> limitaciones,
        Boolean verificacionExternaRealizada,
        OffsetDateTime fechaAnalisis) {
}
