package com.minitextil.gestor.cliente.service;

import com.minitextil.gestor.cliente.model.Cliente;
import com.minitextil.gestor.cliente.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Cliente salvarCliente(Cliente cliente) {
        // Aqui você pode adicionar regras de negócio adicionais, formatações ou logs
        return repository.save(cliente);
    }
}
