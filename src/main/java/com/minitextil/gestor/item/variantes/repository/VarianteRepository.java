package com.minitextil.gestor.item.variantes.repository;

import com.minitextil.gestor.item.variantes.model.Variante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VarianteRepository extends JpaRepository<Variante, Long> { }
