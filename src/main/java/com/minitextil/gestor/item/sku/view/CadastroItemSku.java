package com.minitextil.gestor.item.sku.view;

import com.minitextil.gestor.components.core.program.ProgramContext;
import com.minitextil.gestor.components.core.program.ProgramErp;
import com.minitextil.gestor.components.core.program.ProgramParams;
import com.minitextil.gestor.item.material.repository.MaterialRepository;
import com.minitextil.gestor.item.material.view.LookupMaterial;
import com.minitextil.gestor.item.sku.model.ItemSku;
import com.minitextil.gestor.item.sku.service.ItemSkuService;
import com.minitextil.gestor.item.variantes.repository.VarianteRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.Optional;

public class CadastroItemSku extends ProgramErp {

    private final ItemSkuService service;
    private final MaterialRepository materialRepository;
    private final VarianteRepository varianteRepository;

    // Se != null, estamos editando um SKU existente. Se null, "Salvar" cria um novo.
    private ItemSku itemSkuAtual;

    private final IntegerField codigoSku = new IntegerField("Código SKU");
    private final Button btObterSku = new Button(new Icon(VaadinIcon.SEARCH));

    private final IntegerField idItemPai = new IntegerField("Item Pai");
    private final Button btObterItemPai = new Button(new Icon(VaadinIcon.SEARCH));
    private final TextField descItemPai = new TextField("Descrição do Item Pai");

    private final IntegerField idVariante = new IntegerField("Variante (opcional)");
    private final Button btObterVariante = new Button(new Icon(VaadinIcon.SEARCH));
    private final TextField descVariante = new TextField("Descrição da Variante");

    public CadastroItemSku(ItemSkuService skuService,
                           MaterialRepository materialRepository,
                           VarianteRepository varianteRepository) {
        this.service = skuService;
        this.materialRepository = materialRepository;
        this.varianteRepository = varianteRepository;

        this.setWidthFull();
        this.setAlignItems(Alignment.CENTER);

        codigoSku.setHelperText("Em branco: novo SKU");
        codigoSku.setValueChangeMode(ValueChangeMode.ON_BLUR);
        codigoSku.addValueChangeListener(e -> carregarPorCodigo(e.getValue()));

        idItemPai.setRequiredIndicatorVisible(true);
        idItemPai.setValueChangeMode(ValueChangeMode.ON_BLUR);
        idItemPai.addValueChangeListener(e -> atualizarDescricaoItemPai(e.getValue()));

        idVariante.setClearButtonVisible(true); // variante é opcional
        idVariante.setValueChangeMode(ValueChangeMode.ON_BLUR);
        idVariante.addValueChangeListener(e -> atualizarDescricaoVariante(e.getValue()));

        descItemPai.setReadOnly(true);
        descItemPai.setWidthFull();
        descVariante.setReadOnly(true);
        descVariante.setWidthFull();

        configurarLookups();

        Button btSalvar = new Button("Salvar", _ -> salvar());
        btSalvar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        setCabecalho("Cadastro de Item SKU", "Dados gerais de item SKU");

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("50vw");
        formLayout.setRowSpacing("25px");

        formLayout.add(
                campoComLookup(codigoSku, btObterSku), new Div(),
                campoComLookup(idItemPai, btObterItemPai), campoComLookup(idVariante, btObterVariante),
                descItemPai, descVariante,
                btSalvar);

        add(criarCard(formLayout));
    }

    private void configurarLookups() {
        btObterSku.setTooltipText("Pesquisar SKU");
        btObterSku.addClickListener(_ ->
                new LookupItemSku().VerTodosSkus(this.service,
                        sku -> codigoSku.setValue(sku.getId().intValue())));

        btObterItemPai.setTooltipText("Pesquisar item pai");
        btObterItemPai.addClickListener(_ ->
                new LookupMaterial().VerTodosItens(this.materialRepository,
                        material -> idItemPai.setValue(material.getId().intValue())));

        btObterVariante.setTooltipText("Pesquisar variante");
        btObterVariante.addClickListener(_ ->
                new LookupMaterial().VerTodasVariantes(this.varianteRepository,
                        variante -> idVariante.setValue(variante.getId().intValue())));
    }

    /** Campo de código + botão de lupa lado a lado, como nos outros cadastros. */
    private HorizontalLayout campoComLookup(IntegerField campo, Button botao) {
        HorizontalLayout layout = new HorizontalLayout(campo, botao);
        layout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        layout.setWidthFull();
        layout.setFlexGrow(1, campo);
        return layout;
    }

    private void atualizarDescricaoItemPai(Integer codigo) {
        descItemPai.clear();
        idItemPai.setInvalid(false);

        if (codigo == null) {
            return;
        }

        materialRepository.findById(codigo.longValue()).ifPresentOrElse(
                material -> descItemPai.setValue(material.getDescricao()),
                () -> {
                    idItemPai.setErrorMessage("Item não encontrado");
                    idItemPai.setInvalid(true);
                });
    }

    private void atualizarDescricaoVariante(Integer codigo) {
        descVariante.clear();
        idVariante.setInvalid(false);

        if (codigo == null) {
            return;
        }

        varianteRepository.findById(codigo.longValue()).ifPresentOrElse(
                variante -> descVariante.setValue(variante.getDescricao()),
                () -> {
                    idVariante.setErrorMessage("Variante não encontrada");
                    idVariante.setInvalid(true);
                });
    }

    private void carregarPorCodigo(Integer codigo) {
        if (codigo == null) {
            itemSkuAtual = null;
            idItemPai.clear();
            idVariante.clear();
            return;
        }

        Optional<ItemSku> skuOpt = service.buscarPorId(codigo.longValue());

        if (skuOpt.isPresent()) {
            itemSkuAtual = skuOpt.get();
            idItemPai.setValue(itemSkuAtual.getItemPai().getId().intValue());
            idVariante.setValue(itemSkuAtual.getVariante() != null
                    ? itemSkuAtual.getVariante().getId().intValue()
                    : null);

            Notification.show("SKU " + codigo + " encontrado! Modo de edição ativo.");
        } else {
            // O código é gerado pelo banco, então não dá para criar um SKU com código escolhido.
            Notification.show("SKU " + codigo + " não encontrado.", 4000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_WARNING);
            codigoSku.clear(); // dispara o listener com null e limpa a tela
        }
    }

    private void salvar() {
        Integer pai = idItemPai.getValue();
        Integer variante = idVariante.getValue(); // pode ser null

        if (pai == null) {
            Notification.show("Preencha o Item Pai.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        ItemSku itemSku = (itemSkuAtual != null) ? itemSkuAtual : new ItemSku();

        try {
            ItemSku itemSkuSalvo = service.salvarItemSku(
                    pai.longValue(),
                    variante != null ? variante.longValue() : null,
                    itemSku
            );

            Notification.show("SKU salvo com sucesso! Código: " + itemSkuSalvo.getId())
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            limparTela();

        } catch (Exception e) {
            Notification.show("Erro ao salvar: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void limparTela() {
        itemSkuAtual = null;
        // Limpa explicitamente: em cadastro novo o código já está vazio, então
        // codigoSku.clear() não dispara listener e pai/variante não seriam limpos.
        idItemPai.clear();
        idVariante.clear();
        codigoSku.clear();
    }

    @Override
    public Component getView() {
        return this;
    }

    @Override
    public void onOpen(ProgramParams params, ProgramContext context) {
        // aberto pela consulta: já carrega o SKU escolhido
        if (params != null && params.has("idSku")) {
            Long idSku = params.getLong("idSku");
            if (idSku != null) {
                codigoSku.setValue(idSku.intValue());
            }
        }
    }
}
