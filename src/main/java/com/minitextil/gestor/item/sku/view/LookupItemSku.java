package com.minitextil.gestor.item.sku.view;

import java.util.function.Consumer;

import com.minitextil.gestor.components.grid.GridComponent;
import com.minitextil.gestor.item.sku.model.ItemSku;
import com.minitextil.gestor.item.sku.service.ItemSkuService;
import com.vaadin.flow.component.dialog.Dialog;

public class LookupItemSku {

    public void VerTodosSkus(ItemSkuService service, Consumer<ItemSku> selecionarListener) {
        GridComponent<ItemSku> grid = new GridComponent<>(ItemSku.class);

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Itens SKU");
        dialog.setWidth("60vw");
        dialog.setHeight("40vh");

        grid.setColumnReorderingAllowed(true);
        ItemSkuColunas.configurar(grid);

        grid.addItemDoubleClickListener(event -> {
            ItemSku selecionado = event.getItem();
            if (selecionarListener != null) {
                selecionarListener.accept(selecionado);
            }
            dialog.close();
        });

        grid.setItensComFiltro(service.listarTodos());
        dialog.add(grid);

        dialog.open();
    }
}
