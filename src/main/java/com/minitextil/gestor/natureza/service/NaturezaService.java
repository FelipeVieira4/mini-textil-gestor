package com.minitextil.gestor.natureza.service;

import com.minitextil.gestor.natureza.model.Natureza;
import com.minitextil.gestor.natureza.respository.NaturezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NaturezaService {
    @Autowired
    private NaturezaRepository repository;

    public Natureza salvar(Natureza natureza) {
        return repository.save(natureza);
    }
}