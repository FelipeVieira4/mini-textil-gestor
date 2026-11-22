package com.minitextil.gestor.estoque.repository;

import com.minitextil.gestor.estoque.model.Estoque;
import com.minitextil.gestor.estoque.model.EstoqueId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, EstoqueId> {
}
