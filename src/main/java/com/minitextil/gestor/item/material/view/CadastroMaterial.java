package com.minitextil.gestor.item.material.view;

import java.util.Optional;

import com.minitextil.gestor.components.core.program.*;
import com.minitextil.gestor.item.material.model.Material;
import com.minitextil.gestor.item.material.model.UnidadeMedida;
import com.minitextil.gestor.item.material.repository.MaterialRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

public class CadastroMaterial extends ProgramErp {
	private final Binder<Material> binder = new BeanValidationBinder<>(Material.class);
    private final MaterialRepository repository;

    private final IntegerField id = new IntegerField("Código");
    private final Button btObter = new Button(new Icon(VaadinIcon.SEARCH));
    
    private final TextField descricao = new TextField("Descrição");
    private final Checkbox ativo = new Checkbox("Ativo");
    private final ComboBox<UnidadeMedida> unidade = new ComboBox<>("Unidade");

    Button btEstoqueItem = new Button("Estoque");

    private ProgramContext context;

    public CadastroMaterial(MaterialRepository repository) {
        this.repository = repository;
        
        this.setWidthFull();
        this.setAlignItems(Alignment.CENTER);

        setCabecalho("Cadastro de Item", "Dados gerais do material");
        
        HorizontalLayout idCampo = new HorizontalLayout(id,btObter);
        idCampo.setDefaultVerticalComponentAlignment(HorizontalLayout.Alignment.BASELINE);
        idCampo.setWidthFull();
        
        
        binder.forField(descricao)
                .asRequired("Informe a descrição do Item")
                .bind(Material::getDescricao, Material::setDescricao);

        binder.forField(ativo)
        		.bind(Material::isAtivo, Material::setAtivo);

        
        unidade.setItems(UnidadeMedida.values());
        unidade.setItemLabelGenerator(unidadeItem -> unidadeItem.name());
        
        binder.forField(unidade)
        		.bind(Material::getUnidade, Material::setUnidade);
        

        id.setValueChangeMode(ValueChangeMode.ON_BLUR);
        id.addValueChangeListener(event -> {
            Integer idDigitado = event.getValue();
            if (idDigitado != null) {
                MostrarDadosItem(idDigitado.longValue());
                btEstoqueItem.setEnabled(true);
            } else {
                limparFormulario();
                btEstoqueItem.setEnabled(false);
            }
        });


        btObter.addClickListener(_->{
        	LookupMaterial lookup = new LookupMaterial();

        	lookup.VerTodosItens(this.repository, itemSelecionado -> {
        	    id.setValue(itemSelecionado.getId().intValue());
        	});
        });

        /*
        HorizontalLayout botoes = new HorizontalLayout();
        
        Button btSalvar = new Button("Salvar", _-> salvar());
        btSalvar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        Button btExcluir = new Button("Excluir", _-> excluir(id.getValue().longValue()));
        btExcluir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        botoes.add(btSalvar);
        botoes.add(btExcluir);
        */


        HorizontalLayout botoesOpcoes = new HorizontalLayout();

        btEstoqueItem.addClickListener(_->{
            ProgramParams paramEstItem = new ProgramParams();
            paramEstItem.set("idItem",id.getValue().longValue());

            context.openProgram("CONTROLE_ESTOQUE_ITEM",paramEstItem);
        });
        btEstoqueItem.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btEstoqueItem.setEnabled(false);

        botoesOpcoes.add(btEstoqueItem);

        
        Button btSalvar = new Button("Salvar", _-> salvar());
        btSalvar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FormLayout formLayout = new FormLayout();
        formLayout.setWidthFull();
        formLayout.setRowSpacing("var(--lumo-space-l)");
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2));

        idCampo.setWidth("auto");
        formLayout.add(idCampo, new Div());
        formLayout.add(descricao, ativo);
        formLayout.add(unidade, new Div());

        HorizontalLayout acoes = new HorizontalLayout(btEstoqueItem, btSalvar);
        acoes.setWidthFull();
        acoes.setJustifyContentMode(JustifyContentMode.BETWEEN);

        // Estoque vira botão secundário, Salvar fica como ação principal
        btEstoqueItem.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btEstoqueItem.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btEstoqueItem.setIcon(VaadinIcon.STOCK.create());

        add(criarCard(formLayout, acoes));
    }
    
	private void MostrarDadosItem(Long idBusca) {
        Optional<Material> itemOpt = repository.findById(idBusca);

        if (itemOpt.isPresent()) {
            Material material = itemOpt.get();
            binder.readBean(material);
            
            Notification.show("Item ("+idBusca+") encontrado! Modo de edição ativo.");
        } else {
            limparFormulario();
            Notification.show("ID não encontrado. Um novo registro será criado ao salvar.", 4000, Notification.Position.MIDDLE);
        }
    }

	private void salvar() {
	    boolean isEdicao = id.getValue() != null;

	    Material material = new Material();
	    if (binder.writeBeanIfValid(material)) {
	        
	        if (isEdicao) {
	        	material.setId(id.getValue().longValue());
	        }
	        
	        try {
	            Material materialSalvo = repository.save(material);
	            
	            Notification.show("Item salvo com sucesso! ID: " + materialSalvo.getId())
	                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	            
	            limparFormulario();
	            id.clear();
	            
	        } catch (Exception e) {
	            Notification.show("Erro ao salvar: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
	        }
	        
	    } else {
	        Notification.show("Preencha os campos obrigatórios corretamente.").addThemeVariants(NotificationVariant.LUMO_ERROR);
	    }
	}
	
	/*
	private void excluir(Long idExcluir) {

        try {
            repository.deleteById(idExcluir);
            
            Notification.show("Item Excluir com sucesso! ID: " + idExcluir)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            
            limparFormulario();
            id.clear();
            
        } catch (Exception e) {
            Notification.show("Erro ao salvar: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
	}
	*/


    private void limparFormulario() {
        binder.readBean(null);
        descricao.clear();
        ativo.setValue(true);
        unidade.clear();
    }

    @Override
    public Component getView() {
        return this;
    }

    @Override
    public void onOpen(ProgramParams params, ProgramContext context) {
        this.context = context;

        // aberto pela consulta: já carrega o material escolhido
        if (params != null && params.has("idItem")) {
            Long idItem = params.getLong("idItem");
            if (idItem != null) {
                id.setValue(idItem.intValue());
            }
        }
    }
}