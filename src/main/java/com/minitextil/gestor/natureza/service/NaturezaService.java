package com.minitextil.gestor.natureza.service;

import com.minitextil.gestor.natureza.model.Natureza;
import com.minitextil.gestor.natureza.respository.NaturezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NaturezaService {
    private NaturezaRepository repository;

    @Autowired
    public NaturezaService(NaturezaRepository repository) {
    	this.repository=repository;
    }
    
    public Natureza salvar(Natureza natureza) {
        return repository.save(natureza);
    }
}