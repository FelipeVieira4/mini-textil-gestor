package com.minitextil.gestor.natureza.model;

import com.minitextil.gestor.empresa.model.Empresa;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="TBL_NATUREZA")
public class Natureza {

    @EmbeddedId
    private NaturezaId id;

    @NotBlank(message = "Descrição não pode ser nula!")
    @Size(max = 75,message = "Descrição não pode passar de 75 caracteres!")
    private String descricao;

    @NotNull(message = "Situação não pode ser nulo!")
    private Boolean isAtivo=false;

    public NaturezaId getId() {
        return id;
    }

    public void setId(NaturezaId id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
