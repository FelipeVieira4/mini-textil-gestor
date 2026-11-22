package com.minitextil.gestor.estoque.model;

import com.minitextil.gestor.empresa.model.Empresa;
import com.minitextil.gestor.fornecedor.model.Fornecedor;
import com.minitextil.gestor.item.sku.model.ItemSku;
import com.minitextil.gestor.natureza.model.Natureza;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_ESTOQUE")
public class Estoque {

    @EmbeddedId
    private EstoqueId estoqueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codEmpresa")
    @JoinColumn(name = "cod_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "cod_empresa", referencedColumnName = "cod_empresa", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "cod_natureza", referencedColumnName = "cod_natureza", nullable = false, insertable = false, updatable = false)
    })
    private Natureza natureza;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codFornecedor")
    @JoinColumn(name = "cod_fornecedor", nullable = false)
    private Fornecedor fornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codSku")
    @JoinColumn(name = "cod_sku", nullable = false)
    private ItemSku itemSku;

    @Column(nullable = false)
    private Integer qtdEstoque;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataEntradaEstoque;

    @Column(nullable = true)
    private LocalDateTime dataValidadeLote;

    @PrePersist
    protected void onCreate() {
        this.dataEntradaEstoque = LocalDateTime.now();
    }

    public EstoqueId getEstoqueId() {
        return estoqueId;
    }

    public void setEstoqueId(EstoqueId estoqueId) {
        this.estoqueId = estoqueId;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Natureza getNatureza() {
        return natureza;
    }

    public void setNatureza(Natureza natureza) {
        this.natureza = natureza;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public ItemSku getItemSku() {
        return itemSku;
    }

    public void setItemSku(ItemSku itemSku) {
        this.itemSku = itemSku;
    }

    public Integer getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(Integer qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    public LocalDateTime getDataEntradaEstoque() {
        return dataEntradaEstoque;
    }

    public void setDataEntradaEstoque(LocalDateTime dataEntradaEstoque) {
        this.dataEntradaEstoque = dataEntradaEstoque;
    }

    public LocalDateTime getDataValidadeLote() {
        return dataValidadeLote;
    }

    public void setDataValidadeLote(LocalDateTime dataValidadeLote) {
        this.dataValidadeLote = dataValidadeLote;
    }
}