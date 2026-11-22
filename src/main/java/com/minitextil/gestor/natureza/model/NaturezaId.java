package com.minitextil.gestor.natureza.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NaturezaId implements Serializable {

    @Column(name = "cod_empresa")
    private Long codEmpresa;

    @Column(name = "cod_natureza")
    private Long codNatureza;

    public NaturezaId() {}

    public NaturezaId(Long codEmpresa, Long codNatureza) {
        this.codEmpresa = codEmpresa;
        this.codNatureza = codNatureza;
    }

    public Long getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(Long codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public Long getCodNatureza() {
        return codNatureza;
    }

    public void setCodNatureza(Long codNatureza) {
        this.codNatureza = codNatureza;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NaturezaId that)) return false;
        return Objects.equals(codEmpresa, that.codEmpresa)
                && Objects.equals(codNatureza, that.codNatureza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codEmpresa, codNatureza);
    }
}
