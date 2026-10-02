package com.example.aletheia.pretest.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.aletheia.pretest.models.CategoriaVeredicto;
import com.example.aletheia.pretest.models.ItemCuestionario;
import com.example.aletheia.pretest.models.enums.FaseAplicacion;
import com.example.aletheia.pretest.models.enums.NombreCategoria;
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
                "likert_5", orden++, "TASA_ACIERTO", FaseAplicacion.PRE_POST));
        items.add(crearItem(
                "Se identifica como falsas noticias que en realidad son auténticas (falsos positivos, engañoso, no verificable).",
                "likert_5", orden++, "TASA_ACIERTO", FaseAplicacion.PRE_POST));
        items.add(crearItem(
                "El periodista sustenta su dictamen en evidencias verificables y no únicamente en su percepción personal.",
                "likert_5", orden++, "TASA_ACIERTO", FaseAplicacion.PRE_POST));
        items.add(crearItem(
                "Se mantiene un nivel de acierto constante al evaluar noticias de distintos formatos (texto, imagen y video).",
                "likert_5", orden++, "TASA_ACIERTO", FaseAplicacion.PRE_POST));
        items.add(crearItem(
                "El periodista reconoce señales de desinformación (titulares exagerados, fuentes no atribuibles, datos descontextualizados).",
                "likert_5", orden++, "TASA_ACIERTO", FaseAplicacion.PRE_POST));

        // ===== VARIABLE DEPENDIENTE (PRE_POST) — Dimensión: Tiempo de verificación
        // =====
        items.add(crearItem(
                "Se emite un dictamen sobre la noticia dentro de un tiempo razonable para el ritmo de la redacción.",
                "likert_5", orden++, "TIEMPO_VERIFICACION", FaseAplicacion.PRE_POST));
        items.add(crearItem("Se requiere consultar pocas fuentes externas adicionales para llegar a una conclusión.",
                "likert_5", orden++, "TIEMPO_VERIFICACION", FaseAplicacion.PRE_POST));
        items.add(crearItem("Se completa el proceso de verificación sin interrupciones ni retrocesos significativos.",
                "likert_5", orden++, "TIEMPO_VERIFICACION", FaseAplicacion.PRE_POST));
        items.add(crearItem("Se mantiene tiempos de verificación similares al evaluar noticias de distintos formatos.",
                "likert_5", orden++, "TIEMPO_VERIFICACION", FaseAplicacion.PRE_POST));
        items.add(
                crearItem("Se logra verificar un volumen adecuado de noticias dentro de la sesión de trabajo asignada.",
                        "likert_5", orden++, "TIEMPO_VERIFICACION", FaseAplicacion.PRE_POST));

        // ===== VARIABLE DEPENDIENTE (PRE_POST) — Dimensión: Clasificación de noticias
        // =====
        items.add(crearItem(
                "Se asigna cada noticia a su categoría temática correcta (política, salud, economía, entre otras).",
                "likert_5", orden++, "CLASIFICACION_NOTICIAS", FaseAplicacion.PRE_POST));
        items.add(crearItem("Se determina adecuadamente el nivel de riesgo o impacto de la desinformación detectada.",
                "likert_5", orden++, "CLASIFICACION_NOTICIAS", FaseAplicacion.PRE_POST));
        items.add(crearItem("Se aplica criterios uniformes de clasificación entre una noticia y otra.", "likert_5",
                orden++, "CLASIFICACION_NOTICIAS", FaseAplicacion.PRE_POST));
        items.add(crearItem(
                "Se distingue entre misinformation (difundida sin intención de engañar) y desinformación deliberada.",
                "likert_5", orden++, "CLASIFICACION_NOTICIAS", FaseAplicacion.PRE_POST));
        items.add(crearItem(
                "Se clasifica correctamente noticias ambiguas o que mezclan datos verdaderos con datos distorsionados.",
                "likert_5", orden++, "CLASIFICACION_NOTICIAS", FaseAplicacion.PRE_POST));

        // ===== VARIABLE INDEPENDIENTE (SOLO_POST) — Dimensión: Usabilidad =====
        items.add(crearItem(
                "El sistema resulta fácil de usar para verificar noticias sin requerir capacitación previa extensa.",
                "likert_5", orden++, "USABILIDAD", FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "Las funciones del sistema (carga de texto, imagen y video) se encuentran bien integradas entre sí.",
                "likert_5", orden++, "USABILIDAD", FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "La interfaz del sistema es clara y permite ubicar con rapidez la opción de clasificación deseada.",
                "likert_5", orden++, "USABILIDAD", FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El usuario se siente confiado al utilizar el sistema para emitir un dictamen sobre una noticia.",
                "likert_5", orden++, "USABILIDAD", FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El usuario aprendería a usar el sistema con rapidez y lo utilizaría con frecuencia en su labor periodística.",
                "likert_5", orden++, "USABILIDAD", FaseAplicacion.SOLO_POST));

        // ===== VARIABLE INDEPENDIENTE (SOLO_POST) — Dimensión: Funcionalidad =====
        items.add(crearItem(
                "El sistema permite cargar y procesar noticias en formato texto, imagen (OCR) y video (transcripción de audio).",
                "likert_5", orden++, "FUNCIONALIDAD", FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El sistema realiza correctamente la clasificación binaria del contenido como verdadero o falso.",
                "likert_5", orden++, "FUNCIONALIDAD", FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El modelo de ensamble Stacking integra adecuadamente los clasificadores base durante el procesamiento.",
                "likert_5", orden++, "FUNCIONALIDAD", FaseAplicacion.SOLO_POST));
        items.add(
                crearItem("El sistema registra automáticamente los tiempos de procesamiento y el consumo de recursos.",
                        "likert_5", orden++, "FUNCIONALIDAD", FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El sistema genera un reporte o resultado visible y comprensible para el usuario al finalizar el análisis.",
                "likert_5", orden++, "FUNCIONALIDAD", FaseAplicacion.SOLO_POST));

        // ===== VARIABLE INDEPENDIENTE (SOLO_POST) — Dimensión: Rendimiento =====
        items.add(crearItem(
                "El tiempo de respuesta del sistema al clasificar una noticia es adecuado para el trabajo periodístico diario.",
                "likert_5", orden++, "RENDIMIENTO", FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El sistema mantiene un desempeño estable al procesar noticias en distintos formatos (texto, imagen y video).",
                "likert_5", orden++, "RENDIMIENTO", FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El consumo de memoria del sistema se mantiene dentro de rangos aceptables durante el procesamiento.",
                "likert_5", orden++, "RENDIMIENTO", FaseAplicacion.SOLO_POST));
        items.add(crearItem("El sistema procesa lotes de noticias sin presentar caídas, bloqueos ni demoras excesivas.",
                "likert_5", orden++, "RENDIMIENTO", FaseAplicacion.SOLO_POST));
        items.add(crearItem(
                "El tiempo de respuesta del sistema se mantiene constante conforme aumenta el volumen de noticias analizadas.",
                "likert_5", orden++, "RENDIMIENTO", FaseAplicacion.SOLO_POST));

        itemCuestionarioRepo.saveAll(items);
        log.info("Ítems de cuestionario sembrados: {}", items.size());
    }

    private ItemCuestionario crearItem(String contenido, String tipo, int orden, String dimension,
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