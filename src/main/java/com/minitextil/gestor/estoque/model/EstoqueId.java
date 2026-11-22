package com.minitextil.gestor.estoque.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EstoqueId implements Serializable {

    private static final long serialVersionUID = 1L;

	@Column(name = "cod_empresa", nullable = false)
    private Long codEmpresa;

    @Column(name = "cod_natureza", nullable = false)
    private Long codNatureza;

    @Column(name = "cod_fornecedor", nullable = false)
    private Long codFornecedor;

    @Column(name = "cod_lote", nullable = false)
    private String codLote;

    @Column(name = "cod_sku", nullable = false)
    private Long codSku;

    public EstoqueId() {
    }

    public EstoqueId(Long codEmpresa, Long codNatureza, Long codFornecedor, String codLote, Long codSku) {
        this.codEmpresa = codEmpresa;
        this.codNatureza = codNatureza;
        this.codFornecedor = codFornecedor;
        this.codLote = codLote;
        this.codSku = codSku;
    }

    public Long getCodEmpresa() { return codEmpresa; }
    public void setCodEmpresa(Long codEmpresa) { this.codEmpresa = codEmpresa; }

    public Long getCodNatureza() { return codNatureza; }
    public void setCodNatureza(Long codNatureza) { this.codNatureza = codNatureza; }

    public Long getCodFornecedor() { return codFornecedor; }
    public void setCodFornecedor(Long codFornecedor) { this.codFornecedor = codFornecedor; }

    public String getCodLote() { return codLote; }
    public void setCodLote(String codLote) { this.codLote = codLote; }

    public Long getCodSku() { return codSku; }
    public void setCodSku(Long codSku) { this.codSku = codSku; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstoqueId that)) return false;
        return Objects.equals(codEmpresa, that.codEmpresa)
                && Objects.equals(codNatureza, that.codNatureza)
                && Objects.equals(codFornecedor, that.codFornecedor)
                && Objects.equals(codLote, that.codLote)
                && Objects.equals(codSku, that.codSku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codEmpresa, codNatureza, codFornecedor, codLote, codSku);
    }
}