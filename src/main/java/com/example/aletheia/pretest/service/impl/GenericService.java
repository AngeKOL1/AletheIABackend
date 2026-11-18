package com.example.aletheia.pretest.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.example.aletheia.pretest.exception.ModelNotFoundException;
import com.example.aletheia.pretest.repository.GenericRepo;
import com.example.aletheia.pretest.service.IGenericService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public abstract class GenericService<T, ID> implements IGenericService<T, ID> {
    protected abstract GenericRepo<T, ID> getRepo();

    public static final String ID_NOT_FOUND_MESSAGE = "ID NOT FOUND: ";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public T save(T t) {
        return getRepo().save(t);
    }

    @Override
    @Transactional
    public T update(T t, ID id) {

        if (!getRepo().existsById(id)) {
            throw new ModelNotFoundException(ID_NOT_FOUND_MESSAGE + id);
        }

        // Sin esta comprobación, una entidad con otro id (o sin id) actualizaría otro registro o crearía uno nuevo.
        Object idEntidad = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(t);
        if (!Objects.equals(idEntidad, id)) {
            throw new IllegalArgumentException("El id de la entidad (" + idEntidad + ") no coincide con " + id);
        }

        return getRepo().save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public T findById(ID id) {
        return getRepo().findById(id)
                .orElseThrow(() -> new ModelNotFoundException(ID_NOT_FOUND_MESSAGE + id));
    }

    @Override
    @Transactional
    public void delete(ID id) {

        if (!getRepo().existsById(id)) {
            throw new ModelNotFoundException(ID_NOT_FOUND_MESSAGE + id);
        }

        getRepo().deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<T> listarPaginado(Pageable pageable) {
        return getRepo().findAll(pageable);
    }
}
