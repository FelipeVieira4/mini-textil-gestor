package com.minitextil.gestor.item.sku.view;

import com.minitextil.gestor.components.grid.GridComponent;
import com.minitextil.gestor.components.grid.TipoColunas;
import com.minitextil.gestor.item.sku.model.ItemSku;

/**
 * Colunas padrão de SKU, compartilhadas entre a consulta e o lookup
 * para as duas telas nunca ficarem diferentes uma da outra.
 *
 * Pressupõe que o SKU veio com pai e variante carregados
 * (ItemSkuService.listarTodos), pois são LAZY.
 */
final class ItemSkuColunas {

    private ItemSkuColunas() {}

    static void configurar(GridComponent<ItemSku> grid) {
        grid.addColunaComFiltro(ItemSku::getId, "Código SKU", TipoColunas.Numerico);

        grid.addColunaComFiltro(sku -> sku.getItemPai().getId(), "Item Pai", TipoColunas.Numerico);
        grid.addColunaComFiltro(sku -> sku.getItemPai().getDescricao(), "Descrição Item", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(sku -> sku.getItemPai().getUnidade(), "Uni", TipoColunas.Alfanumerico);

        // Variante é opcional: sem variante a célula fica em branco
        grid.addColunaComFiltro(sku -> sku.getVariante() != null ? sku.getVariante().getId() : null,
                "Variante", TipoColunas.Numerico);
        grid.addColunaComFiltro(sku -> sku.getVariante() != null ? sku.getVariante().getDescricao() : null,
                "Descrição Variante", TipoColunas.Alfanumerico);
    }
}
