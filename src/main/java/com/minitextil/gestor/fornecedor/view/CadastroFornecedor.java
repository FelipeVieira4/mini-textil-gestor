package com.minitextil.gestor.fornecedor.view;

import com.minitextil.gestor.components.core.program.ProgramErp;
import com.minitextil.gestor.fornecedor.model.Fornecedor;
import com.minitextil.gestor.fornecedor.repository.FornecedorRepository;
import com.minitextil.gestor.fornecedor.service.FornecedorService;
import com.minitextil.gestor.genericos.embeddable.Endereco;
import com.minitextil.gestor.genericos.utils.CnpjUtils;
import com.minitextil.gestor.genericos.utils.TelefoneUtils;
import com.minitextil.gestor.genericos.view.EnderecoComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.Optional;

public class CadastroFornecedor extends ProgramErp {
    private static final long serialVersionUID = 1L;

    private final Binder<Fornecedor> binder = new BeanValidationBinder<>(Fornecedor.class);

    private final FornecedorRepository repository;
    private final FornecedorService service;

    private final IntegerField id = new IntegerField("Código");
    private final Button btObter = new Button(new Icon(VaadinIcon.SEARCH));

    private final TextField descricao = new TextField("Descrição");
    private final TextField cnpj = new TextField("CNPJ");
    private final Checkbox situacao = new Checkbox("Ativo");

    private final TextField telefone = new TextField("Telefone");
    private final EmailField email = new EmailField("E-mail");

    // Componente encapsulado de endereço
    private final EnderecoComponent enderecoComponent = new EnderecoComponent();

    public CadastroFornecedor(FornecedorRepository repository, FornecedorService service) {
        this.repository = repository;
        this.service = service;

        this.setWidthFull();
        this.setAlignItems(Alignment.CENTER);

        setCabecalho("Cadastro de Fornecedor");

        HorizontalLayout idCampo = new HorizontalLayout(id, btObter);
        idCampo.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        idCampo.setWidthFull();

        // Binder apenas para os campos diretos do Cliente
        binder.forField(descricao)
                .asRequired("Informe a descrição")
                .bind(Fornecedor::getDescricao, Fornecedor::setDescricao);

        binder.forField(cnpj)
                .asRequired("Informe o CNPJ")
                .withConverter(
                        CnpjUtils::limpar,
                        CnpjUtils::formatar
                )
                .withValidator(CnpjUtils::formatoValido,
                        "CNPJ deve ter 14 caracteres (os 2 últimos são dígitos)")
                .bind(Fornecedor::getCnpj, Fornecedor::setCnpj);

        cnpj.setPlaceholder("00.000.000/0000-00");
        cnpj.setMaxLength(18);

        binder.forField(telefone)
                .withConverter(TelefoneUtils::limpar, TelefoneUtils::formatar)
                .withValidator(TelefoneUtils::formatoValido, "Telefone deve ter 10 ou 11 dígitos (com DDD)")
                .bind(Fornecedor::getTelefone, Fornecedor::setTelefone);

        telefone.setPlaceholder("(00) 00000-0000");
        telefone.setMaxLength(15);

        binder.forField(email)
                .withValidator(new EmailValidator("E-mail inválido", true))  // true = permite vazio
                .withConverter(
                        valor -> valor == null || valor.isBlank() ? null : valor.trim().toLowerCase(),
                        valor -> valor == null ? "" : valor
                )
                .bind(Fornecedor::getEmail, Fornecedor::setEmail);

        email.setPlaceholder("contato@empresa.com.br");
        email.setClearButtonVisible(true);

        binder.forField(situacao)
                .bind(Fornecedor::getSituacao, Fornecedor::setSituacao);

        id.setValueChangeMode(ValueChangeMode.ON_BLUR);
        id.addValueChangeListener(event -> {
            Integer idDigitado = event.getValue();
            if (idDigitado != null) {
                obterDadosCliente(idDigitado.longValue());
            } else {
                limparFormulario();
            }
        });

        btObter.addClickListener(_ -> {
            LookupFornecedor lookup = new LookupFornecedor();
            lookup.VerTodosFornecedor(this.repository, fornecedorSelecionados -> {
                this.id.setValue(fornecedorSelecionados.getId().intValue());
            });
        });

        Button btSalvar = new Button("Salvar", _ -> salvar());
        btSalvar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("55vw");
        formLayout.setRowSpacing("20px");

        formLayout.add(
                idCampo,
                descricao,
                cnpj,
                telefone,
                email,
                situacao,
                enderecoComponent,
                btSalvar
        );

        formLayout.setColspan(enderecoComponent, 2);
        formLayout.setColspan(btSalvar, 2);

        add(criarCard(formLayout));
        limparFormulario();
    }

    private void obterDadosCliente(Long idBusca) {
        Optional<Fornecedor> clienteOpt = repository.findById(idBusca);

        if (clienteOpt.isPresent()) {
            Fornecedor cliente = clienteOpt.get();
            binder.readBean(cliente);

            if (cliente.getEndereco() != null) {
                enderecoComponent.readBean(cliente.getEndereco());
            } else {
                enderecoComponent.clear();
            }

            Notification.show("Cliente (" + idBusca + ") encontrado! Modo de edição ativo.");
        } else {
            limparFormulario();
            Notification.show("ID não encontrado. Um novo registro será criado ao salvar.", 4000, Notification.Position.MIDDLE);
        }
    }

    private void salvar() {
        // Em edição, carrega o registro existente para não perder campos fora da tela (ex.: dataCriacao)
        Fornecedor cliente = null;
        if (id.getValue() != null) {
            cliente = repository.findById(id.getValue().longValue()).orElse(null);
        }
        if (cliente == null) {
            cliente = new Fornecedor(); // novo registro: o banco gera o ID
        }

        Endereco endereco = new Endereco();

        // Valida o binder principal e o binder interno do componente de endereço
        boolean clienteValid = binder.writeBeanIfValid(cliente);
        boolean enderecoValid = enderecoComponent.writeBeanIfValid(endereco);

        if (!clienteValid || !enderecoValid) {
            Notification.show("Preencha todos os campos obrigatórios corretamente.")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        cliente.setEndereco(endereco);

        // Usa o CNPJ já limpo, que é o formato gravado no banco
        Optional<Fornecedor> clienteExistente = repository.findByCnpj(cliente.getCnpj());
        if (clienteExistente.isPresent()
                && !clienteExistente.get().getId().equals(cliente.getId())) {
            Notification.show("CNPJ já cadastrado para outro cliente!")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        try {
            Fornecedor salvo = service.salvarCliente(cliente);
            Notification.show("Cliente salvo com sucesso! ID: " + salvo.getId())
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            limparFormulario();
            id.clear();
        } catch (Exception e) {
            Notification.show("Erro ao salvar: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void limparFormulario() {
        binder.readBean(null);
        enderecoComponent.clear();
        situacao.setValue(true);
    }

    @Override
    public Component getView() {
        return this;
    }
}