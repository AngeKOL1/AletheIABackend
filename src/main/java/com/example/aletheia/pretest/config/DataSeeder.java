package com.example.aletheia.pretest.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.aletheia.pretest.models.CategoriaVeredicto;
import com.example.aletheia.pretest.models.ItemCuestionario;
import com.example.aletheia.pretest.models.enums.Dimension;
import com.example.aletheia.pretest.models.enums.FaseAplicacion;
import com.example.aletheia.pretest.models.enums.NombreCategoria;
import com.example.aletheia.pretest.models.enums.TipoRespuesta;
import com.example.aletheia.pretest.repository.CategoriaVeredictoRepo;
import com.example.aletheia.pretest.repository.ItemCuestionarioRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Order(1)
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final CategoriaVeredictoRepo categoriaVeredictoRepo;
    private final ItemCuestionarioRepo itemCuestionarioRepo;

    @Override
    public void run(String... args) {
        sembrarCategorias();
        sembrarItemsCuestionario();
    }

    private void sembrarCategorias() {
        for (NombreCategoria nombre : NombreCategoria.values()) {
            boolean existe = categoriaVeredictoRepo.existsByNombre(nombre);
            if (!existe) {
                CategoriaVeredicto categoria = new CategoriaVeredicto();
                categoria.setNombre(nombre);
                categoriaVeredictoRepo.save(categoria);
                log.info("Categoría sembrada: {}", nombre);
            }
        }
        log.info("Verificación de categorías completada. Total en BD: {}", categoriaVeredictoRepo.count());
    }

    private void sembrarItemsCuestionario() {
        if (itemCuestionarioRepo.count() > 0)
            return;

        List<ItemCuestionario> items = new ArrayList<>();
        int orden = 1;

        // ===== VARIABLE DEPENDIENTE (PRE_POST) — Dimensión: Tasa de acierto =====
        items.add(crearItem("Se identifica correctamente como falsas las noticias que efectivamente lo son.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.TASA_ACIERTO, FaseAplicacion.PRE_POST));
        items.add(crearItem(
                "Se identifica como falsas noticias que en realidad son auténticas (falsos positivos, engañoso, no verificable).",
                TipoRespuesta.LIKERT_5, orden++, Dimension.TASA_ACIERTO, FaseAplicacion.PRE_POST));
        items.add(crearItem(
                "El periodista sustenta su dictamen en evidencias verificables y no únicamente en su percepción personal.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.TASA_ACIERTO, FaseAplicacion.PRE_POST));
        items.add(crearItem(
                "Se mantiene un nivel de acierto constante al evaluar noticias de distintos formatos (texto, imagen y video).",
                TipoRespuesta.LIKERT_5, orden++, Dimension.TASA_ACIERTO, FaseAplicacion.PRE_POST));
        items.add(crearItem(
                "El periodista reconoce señales de desinformación (titulares exagerados, fuentes no atribuibles, datos descontextualizados).",
                TipoRespuesta.LIKERT_5, orden++, Dimension.TASA_ACIERTO, FaseAplicacion.PRE_POST));

        // ===== VARIABLE DEPENDIENTE (PRE_POST) — Dimensión: Tiempo de verificación
        // =====
        items.add(crearItem(
                "Se emite un dictamen sobre la noticia dentro de un tiempo razonable para el ritmo de la redacción.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.TIEMPO_VERIFICACION, FaseAplicacion.PRE_POST));
        items.add(crearItem("Se requiere consultar pocas fuentes externas adicionales para llegar a una conclusión.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.TIEMPO_VERIFICACION, FaseAplicacion.PRE_POST));
        items.add(crearItem("Se completa el proceso de verificación sin interrupciones ni retrocesos significativos.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.TIEMPO_VERIFICACION, FaseAplicacion.PRE_POST));
        items.add(crearItem("Se mantiene tiempos de verificación similares al evaluar noticias de distintos formatos.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.TIEMPO_VERIFICACION, FaseAplicacion.PRE_POST));
        items.add(
                crearItem("Se logra verificar un volumen adecuado de noticias dentro de la sesión de trabajo asignada.",
                        TipoRespuesta.LIKERT_5, orden++, Dimension.TIEMPO_VERIFICACION, FaseAplicacion.PRE_POST));

        // ===== VARIABLE DEPENDIENTE (PRE_POST) — Dimensión: Clasificación de noticias
        // =====
        items.add(crearItem(
                "Se asigna cada noticia a su categoría temática correcta (política, salud, economía, entre otras).",
                TipoRespuesta.LIKERT_5, orden++, Dimension.CLASIFICACION_NOTICIAS, FaseAplicacion.PRE_POST));
        items.add(crearItem("Se determina adecuadamente el nivel de riesgo o impacto de la desinformación detectada.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.CLASIFICACION_NOTICIAS, FaseAplicacion.PRE_POST));
        items.add(crearItem("Se aplica criterios uniformes de clasificación entre una noticia y otra.", TipoRespuesta.LIKERT_5,
                orden++, Dimension.CLASIFICACION_NOTICIAS, FaseAplicacion.PRE_POST));
        items.add(crearItem(
                "Se distingue entre misinformation (difundida sin intención de engañar) y desinformación deliberada.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.CLASIFICACION_NOTICIAS, FaseAplicacion.PRE_POST));
        items.add(crearItem(
                "Se clasifica correctamente noticias ambiguas o que mezclan datos verdaderos con datos distorsionados.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.CLASIFICACION_NOTICIAS, FaseAplicacion.PRE_POST));

        // ===== VARIABLE INDEPENDIENTE (SOLO_POST) — Dimensión: Usabilidad =====
        items.add(crearItem(
                "El sistema resulta fácil de usar para verificar noticias sin requerir capacitación previa extensa.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.USABILIDAD, FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "Las funciones del sistema (carga de texto, imagen y video) se encuentran bien integradas entre sí.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.USABILIDAD, FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "La interfaz del sistema es clara y permite ubicar con rapidez la opción de clasificación deseada.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.USABILIDAD, FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El usuario se siente confiado al utilizar el sistema para emitir un dictamen sobre una noticia.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.USABILIDAD, FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El usuario aprendería a usar el sistema con rapidez y lo utilizaría con frecuencia en su labor periodística.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.USABILIDAD, FaseAplicacion.SOLO_POST));

        // ===== VARIABLE INDEPENDIENTE (SOLO_POST) — Dimensión: Funcionalidad =====
        items.add(crearItem(
                "El sistema permite cargar y procesar noticias en formato texto, imagen (OCR) y video (transcripción de audio).",
                TipoRespuesta.LIKERT_5, orden++, Dimension.FUNCIONALIDAD, FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El sistema realiza correctamente la clasificación binaria del contenido como verdadero o falso.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.FUNCIONALIDAD, FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El modelo de ensamble Stacking integra adecuadamente los clasificadores base durante el procesamiento.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.FUNCIONALIDAD, FaseAplicacion.SOLO_POST));
        items.add(
                crearItem("El sistema registra automáticamente los tiempos de procesamiento y el consumo de recursos.",
                        TipoRespuesta.LIKERT_5, orden++, Dimension.FUNCIONALIDAD, FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El sistema genera un reporte o resultado visible y comprensible para el usuario al finalizar el análisis.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.FUNCIONALIDAD, FaseAplicacion.SOLO_POST));

        // ===== VARIABLE INDEPENDIENTE (SOLO_POST) — Dimensión: Rendimiento =====
        items.add(crearItem(
                "El tiempo de respuesta del sistema al clasificar una noticia es adecuado para el trabajo periodístico diario.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.RENDIMIENTO, FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El sistema mantiene un desempeño estable al procesar noticias en distintos formatos (texto, imagen y video).",
                TipoRespuesta.LIKERT_5, orden++, Dimension.RENDIMIENTO, FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El consumo de memoria del sistema se mantiene dentro de rangos aceptables durante el procesamiento.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.RENDIMIENTO, FaseAplicacion.SOLO_POST));
        items.add(crearItem("El sistema procesa lotes de noticias sin presentar caídas, bloqueos ni demoras excesivas.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.RENDIMIENTO, FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El tiempo de respuesta del sistema se mantiene constante conforme aumenta el volumen de noticias analizadas.",
                TipoRespuesta.LIKERT_5, orden++, Dimension.RENDIMIENTO, FaseAplicacion.SOLO_POST));

        itemCuestionarioRepo.saveAll(items);
        log.info("Ítems de cuestionario sembrados: {}", items.size());
    }

    private ItemCuestionario crearItem(String contenido, TipoRespuesta tipo, int orden, Dimension dimension,
            FaseAplicacion faseAplicacion) {
        ItemCuestionario item = new ItemCuestionario();
        item.setContenido(contenido);
        item.setTipoRespuesta(tipo);
        item.setOrden(orden);
        item.setDimension(dimension);
        item.setFaseAplicacion(faseAplicacion);
        return item;
    }

}