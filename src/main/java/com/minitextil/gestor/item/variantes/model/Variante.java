package com.minitextil.gestor.item.variantes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="TBL_VARIANTE_ITEM")
public class Variante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="descricao")
    @NotBlank(message = "Descrição da variante é obrigatório!")
    @Size(max = 50, message = "Descrição da variante não pode ser maior que 50 caracteres!")
    private String descricao;

    @Column(name = "hexa_cor")
    private String hexaCor;

    @Column(name = "is_ativo")
    @NotNull(message = "Situação da variante é obrigatório!")
    private Boolean ativo;

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

    public String getHexaCor() {
        return hexaCor;
    }

    public void setHexaCor(String hexaCor) {
        this.hexaCor = hexaCor;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
