package com.minitextil.gestor.estoque.service;

import com.minitextil.gestor.estoque.models.ItemEstoque;
import com.minitextil.gestor.estoque.models.ItemEstoqueId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemEstoqueRepository extends JpaRepository<ItemEstoque, ItemEstoqueId> {
}
