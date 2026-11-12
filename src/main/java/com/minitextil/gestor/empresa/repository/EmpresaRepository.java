package com.minitextil.gestor.empresa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minitextil.gestor.empresa.model.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {}
