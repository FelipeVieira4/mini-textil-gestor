package com.minitextil.gestor.item.sku.model;

import com.minitextil.gestor.item.material.model.Material;
import com.minitextil.gestor.item.variantes.model.Variante;
import jakarta.persistence.*;

@Entity
@Table(
        name = "TBL_SKU",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_sku_pai_variante",
                columnNames = {"item_pai_id", "variante_id"}
        )
)
public class ItemSku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sku")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_pai_id", nullable = false)
    private Material materialPai;

    // Opcional: SKU pode existir sem variante
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variante_id", nullable = true)
    private Variante variante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Material getItemPai() {
        return materialPai;
    }

    public void setItemPai(Material materialPai) {
        this.materialPai = materialPai;
    }

    public Variante getVariante() {
        return variante;
    }

    public void setVariante(Variante variante) {
        this.variante = variante;
    }
}
