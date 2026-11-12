package com.minitextil.gestor.estoque.models;

import com.minitextil.gestor.empresa.model.Empresa;
import com.minitextil.gestor.item.material.model.Material;
import jakarta.persistence.*;

@Entity
@Table(name = "TBL_ITEM_ESTOQUE")
public class ItemEstoque {

    @EmbeddedId
    private ItemEstoqueId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("empresaId")
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Material material;

    private Boolean controlaFornecedor;
    private Boolean controlaLote;
    private Integer estoqueMinimo;
    private Integer estoqueMaximo;

    public ItemEstoqueId getId() {
        return id;
    }

    public void setId(ItemEstoqueId id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Material getItem() {
        return material;
    }

    public void setItem(Material material) {
        this.material = material;
    }

    public boolean isControlaFornecedor() {
        return controlaFornecedor;
    }

    public void setControlaFornecedor(boolean controlaFornecedor) {
        this.controlaFornecedor = controlaFornecedor;
    }

    public boolean isControlaLote() {
        return controlaLote;
    }

    public void setControlaLote(boolean controlaLote) {
        this.controlaLote = controlaLote;
    }
}