package com.minitextil.gestor.fornecedor.service;

import com.minitextil.gestor.fornecedor.model.Fornecedor;
import com.minitextil.gestor.fornecedor.repository.FornecedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class FornecedorService {
    private final FornecedorRepository repository;

    public FornecedorService(FornecedorRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Fornecedor salvarCliente(Fornecedor cliente) {
        return repository.save(cliente);
    }
}
