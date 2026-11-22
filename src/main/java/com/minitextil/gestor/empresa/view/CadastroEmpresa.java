package com.minitextil.gestor.empresa.view;

import java.util.Optional;

import com.minitextil.gestor.components.core.program.ProgramErp;
import com.minitextil.gestor.empresa.model.Empresa;
import com.minitextil.gestor.empresa.repository.EmpresaRepository;
import com.minitextil.gestor.genericos.utils.CnpjUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
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

public class CadastroEmpresa extends ProgramErp {
	   private static final long serialVersionUID = 1L;
	    
		private final Binder<Empresa> binder = new BeanValidationBinder<>(Empresa.class);
	    private final EmpresaRepository repository;

		private final Button btObter = new Button(new Icon(VaadinIcon.SEARCH));
		
	    private final IntegerField id = new IntegerField("Código (Deixe vazio para novo)");
	    private final TextField descricao = new TextField("Descrição");
	    private final TextField cnpjEmpresa = new TextField("CNPJ Empresa");
	    
	    private final Checkbox situacao = new Checkbox("Situacao");

	    public CadastroEmpresa(EmpresaRepository repository) {
	        this.repository = repository;
	        
	        this.setWidthFull();
	        this.setAlignItems(Alignment.CENTER);

			setCabecalho("Cadastro de Empresa", "Dados gerais de empresa");

			HorizontalLayout idCampo = new HorizontalLayout(id,btObter);
	        idCampo.setDefaultVerticalComponentAlignment(HorizontalLayout.Alignment.BASELINE);
	        idCampo.setWidthFull();
	        
	        binder.forField(descricao)
	                .asRequired("Informe a descrição")
	                .bind(Empresa::getDescricao, Empresa::setDescricao);

			binder.forField(cnpjEmpresa)
					.asRequired("Informe o CNPJ")
					.withConverter(
							CnpjUtils::limpar,      // campo -> bean (grava limpo)
							CnpjUtils::formatar     // bean -> campo (exibe formatado)
					)
					.withValidator(CnpjUtils::formatoValido,
							"CNPJ deve ter 14 caracteres (os 2 últimos são dígitos)")
					.bind(Empresa::getCnpj, Empresa::setCnpj);

	        cnpjEmpresa.setPlaceholder("00.000.000/0000-00");
	        
	        binder.forField(situacao)
	                .bind(Empresa::getSituacao, Empresa::setSituacao);

	        id.setValueChangeMode(ValueChangeMode.ON_BLUR);
	        id.addValueChangeListener(event -> {
	            Integer idDigitado = event.getValue();
	            if (idDigitado != null) {
	                MostrarDadosEmpresa(idDigitado.longValue());
	            } else {
	                limparFormulario();
	            }
	        });

	        btObter.addClickListener(_->{
	        	LookupEmpresa lookup = new LookupEmpresa();

	        	lookup.VerTodasEmpresas(this.repository, operadorSelecionado -> {
	        	    this.id.setValue(operadorSelecionado.getId().intValue());
	        	});
	        });
	        
	        Button btSalvar = new Button("Salvar", _-> salvar());
	        btSalvar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	        
	        FormLayout formLayout = new FormLayout();
	        formLayout.setWidth("50vw");
	        formLayout.setRowSpacing("25px");
	        
	        formLayout.add(idCampo, new Div(), descricao, cnpjEmpresa, situacao, new Div(), btSalvar);
			add(criarCard(formLayout));
	    }

		private void MostrarDadosEmpresa(Long idBusca) {
	        Optional<Empresa> empresaObtida = repository.findById(idBusca);

	        if (empresaObtida.isPresent()) {
	            Empresa empresa= empresaObtida.get();
	            binder.readBean(empresa);
	            Notification.show("Empresa ("+idBusca+") encontrado! Modo de edição ativo.");
	        } else {
	            limparFormulario();
	            Notification.show("ID não encontrado. Um novo registro será criado ao salvar.", 4000, Notification.Position.MIDDLE);
	        }
	    }

	    private void salvar() {

	        Empresa empresaModel=new Empresa();

	        if (binder.writeBeanIfValid(empresaModel)) {
	            
	            if (id.getValue() != null) {
	            	empresaModel.setId(id.getValue().longValue());
	            }
	            
	            try {
	                Empresa empresaSalvo = repository.save(empresaModel);
	                
	                Notification.show("Empresa salvo com sucesso! ID: " + empresaSalvo.getId())
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

	    private void limparFormulario() {
	        binder.readBean(null);
	        descricao.clear();
	        cnpjEmpresa.clear();
	        situacao.setValue(true);
	    }

	    @Override
	    public Component getView() {
	        return this;
	    }
	}