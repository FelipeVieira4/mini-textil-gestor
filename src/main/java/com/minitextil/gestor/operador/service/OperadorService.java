package com.minitextil.gestor.operador.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.minitextil.gestor.operador.model.Operador;
import com.minitextil.gestor.operador.repository.OperadorRepository;

@Service
public class OperadorService {
	
	private OperadorRepository repository;
	private PasswordEncoder	 passwordEncoder;

	@Autowired
	public OperadorService (OperadorRepository repository,
							PasswordEncoder passwordEncoder){
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Transactional
	public Operador salvarOperador(Operador operador, String senhaNova) {
		boolean temSenhaNova = senhaNova != null && !senhaNova.isBlank();

		if (temSenhaNova) {
			// Só aqui codifica: senhaNova é texto puro. O encoder já devolve com {bcrypt}.
			operador.setSenha(passwordEncoder.encode(senhaNova));
		} else if (operador.getId() == null) {
			throw new IllegalArgumentException("A senha é obrigatória para novo operador.");
		}
		// Edição sem senha nova: mantém o hash que já está no operador, sem codificar de novo.
		return repository.save(operador);
	}
}
