package com.minitextil.gestor.item.material.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "TBL_MATERIAL")
public class Material {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Descrição do produto obrigatório!")
	private String descricao;

	@Column(nullable = false)
	@NotNull(message = "Situação do matérial não pode ser nulo!")
	private boolean ativo = true;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Unidade do produto obrigatório!")
	private UnidadeMedida unidade;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public UnidadeMedida getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeMedida unidade) {
		this.unidade = unidade;
	}

	/*
	public ItemSku getOrigemSku() {
		return origemSku;
	}

	public void setOrigemSku(ItemSku origemSku) {
		this.origemSku = origemSku;
	}
	*/
}