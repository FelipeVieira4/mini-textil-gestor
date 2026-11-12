package com.minitextil.gestor.item.sku.view;

import com.minitextil.gestor.components.core.program.ProgramContext;
import com.minitextil.gestor.components.core.program.ProgramErp;
import com.minitextil.gestor.components.core.program.ProgramParams;
import com.minitextil.gestor.components.grid.GridComponent;
import com.minitextil.gestor.item.sku.model.ItemSku;
import com.minitextil.gestor.item.sku.service.ItemSkuService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

public class ConsultaItemSku extends ProgramErp {

    private static final long serialVersionUID = 1L;

    private final GridComponent<ItemSku> grid = new GridComponent<>(ItemSku.class);
    private final ItemSkuService service;

    private ProgramContext context;

    public ConsultaItemSku(ItemSkuService service) {
        this.service = service;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        setCabecalho("Consulta de Itens SKU", "Duplo clique em uma linha para abrir o cadastro");

        Button btAtualizar = new Button("Atualizar", VaadinIcon.REFRESH.create(), _ -> carregarSkus());
        btAtualizar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        grid.setColumnReorderingAllowed(true);
        ItemSkuColunas.configurar(grid);
        grid.addItemDoubleClickListener(event -> abrirCadastro(event.getItem()));

        add(btAtualizar, grid);
        setFlexGrow(1, grid);

        carregarSkus();
    }

    private void carregarSkus() {
        grid.setItensComFiltro(service.listarTodos());
    }

    private void abrirCadastro(ItemSku sku) {
        if (context == null) {
            return;
        }
        // ao fechar o cadastro, recarrega a lista (o SKU pode ter sido alterado)
        context.openProgram("CADASTRO_ITEM_SKU",
                new ProgramParams().set("idSku", sku.getId()),
                _ -> carregarSkus());
    }

    @Override
    public Component getView() {
        return this;
    }

    @Override
    public void onOpen(ProgramParams params, ProgramContext context) {
        this.context = context;
    }
}
