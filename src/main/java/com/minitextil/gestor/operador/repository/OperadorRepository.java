package com.minitextil.gestor.operador.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minitextil.gestor.operador.model.Operador;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Long>{
	Optional<Operador> findByLoginName(String loginName);
}
