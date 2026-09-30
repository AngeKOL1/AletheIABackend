package com.example.aletheia.pretest.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.aletheia.pretest.models.CategoriaVeredicto;
import com.example.aletheia.pretest.models.enums.NombreCategoria;
import com.example.aletheia.pretest.repository.CategoriaVeredictoRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor 
@Order(1)
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final CategoriaVeredictoRepo categoriaVeredictoRepo;

    @Override
    public void run(String... args) {
        sembrarCategorias();
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
}