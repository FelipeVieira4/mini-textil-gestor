package com.minitextil.gestor.natureza.model;

import com.minitextil.gestor.empresa.model.Empresa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "TBL_NATUREZA")
public class Natureza {

    @EmbeddedId
    private NaturezaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_empresa", nullable = false)
    private Empresa empresa;

    @NotBlank(message = "Descrição não pode ser nula!")
    @Size(max = 75, message = "Descrição não pode passar de 75 caracteres!")
    @Column(nullable = false, length = 75)
    private String descricao;

    @NotNull(message = "Situação não pode ser nulo!")
    @Column(name = "is_ativo", nullable = false)
    private Boolean ativo = false;

    public NaturezaId getId() { return id; }
    public void setId(NaturezaId id) { this.id = id; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}