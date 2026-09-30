package com.example.aletheia.pretest.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.aletheia.pretest.models.CategoriaVeredicto;
import com.example.aletheia.pretest.models.ItemCuestionario;
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
        if (itemCuestionarioRepo.count() == 0) {
            List<ItemCuestionario> items = List.of(
                    crearItem("Me siento capaz de identificar noticias falsas por mi cuenta", "likert_5", 1),
                    crearItem("Confío en que una IA puede ayudarme a verificar noticias", "likert_5", 2)
            // ... agrega el resto de tus ítems reales aquí
            );
            itemCuestionarioRepo.saveAll(items);
            log.info("Ítems de cuestionario sembrados: {}", items.size());
        }
    }

    private ItemCuestionario crearItem(String contenido, String tipo, int orden) {
        ItemCuestionario item = new ItemCuestionario();
        item.setContenido(contenido);
        item.setTipoRespuesta(tipo);
        item.setOrden(orden);
        return item;
    }
}