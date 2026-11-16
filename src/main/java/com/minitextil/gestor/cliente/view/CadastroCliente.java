package com.minitextil.gestor.cliente.view;

import com.minitextil.gestor.cliente.model.Cliente;
import com.minitextil.gestor.cliente.repository.ClienteRepository;
import com.minitextil.gestor.cliente.service.ClienteService;
import com.minitextil.gestor.components.core.program.ProgramErp;
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

public class CadastroCliente extends ProgramErp {
    private static final long serialVersionUID = 1L;

    private final Binder<Cliente> binder = new BeanValidationBinder<>(Cliente.class);

    private final ClienteRepository repository;
    private final ClienteService service;

    private final IntegerField id = new IntegerField("Código");
    private final Button btObter = new Button(new Icon(VaadinIcon.SEARCH));

    private final TextField descricao = new TextField("Razão Social / Nome");
    private final TextField cnpj = new TextField("CNPJ");
    private final Checkbox situacao = new Checkbox("Ativo");

    private final TextField telefone = new TextField("Telefone");
    private final EmailField email = new EmailField("E-mail");

    // Componente encapsulado de endereço
    private final EnderecoComponent enderecoComponent = new EnderecoComponent();

    public CadastroCliente(ClienteRepository repository, ClienteService service) {
        this.repository = repository;
        this.service = service;

        this.setWidthFull();
        this.setAlignItems(Alignment.CENTER);

        setCabecalho("Cadastro de Cliente", "Dados cadastrais e endereço de clientes");

        HorizontalLayout idCampo = new HorizontalLayout(id, btObter);
        idCampo.setDefaultVerticalComponentAlignment(HorizontalLayout.Alignment.BASELINE);
        idCampo.setWidthFull();

        // Binder apenas para os campos diretos do Cliente
        binder.forField(descricao)
                .asRequired("Informe a descrição")
                .bind(Cliente::getDescricao, Cliente::setDescricao);

        binder.forField(cnpj)
                .asRequired("Informe o CNPJ")
                .withConverter(
                        CnpjUtils::limpar,
                        CnpjUtils::formatar
                )
                .withValidator(CnpjUtils::formatoValido,
                        "CNPJ deve ter 14 caracteres (os 2 últimos são dígitos)")
                .bind(Cliente::getCnpj, Cliente::setCnpj);

        cnpj.setPlaceholder("00.000.000/0000-00");
        cnpj.setMaxLength(18);

        binder.forField(telefone)
                .withConverter(TelefoneUtils::limpar, TelefoneUtils::formatar)
                .withValidator(TelefoneUtils::formatoValido, "Telefone deve ter 10 ou 11 dígitos (com DDD)")
                .bind(Cliente::getTelefone, Cliente::setTelefone);

        telefone.setPlaceholder("(00) 00000-0000");
        telefone.setMaxLength(15);

        binder.forField(email)
                .withValidator(new EmailValidator("E-mail inválido", true))  // true = permite vazio
                .withConverter(
                        valor -> valor == null || valor.isBlank() ? null : valor.trim().toLowerCase(),
                        valor -> valor == null ? "" : valor
                )
                .bind(Cliente::getEmail, Cliente::setEmail);

        email.setPlaceholder("contato@empresa.com.br");
        email.setClearButtonVisible(true);

        binder.forField(situacao)
                .bind(Cliente::getSituacao, Cliente::setSituacao);

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
            LookupCliente lookup = new LookupCliente();
            lookup.VerTodosCliente(this.repository, clienteSelecionado -> {
                this.id.setValue(clienteSelecionado.getId().intValue());
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
        Optional<Cliente> clienteOpt = repository.findById(idBusca);

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
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
        Cliente cliente = null;
        if (id.getValue() != null) {
            cliente = repository.findById(id.getValue().longValue()).orElse(null);
        }
        if (cliente == null) {
            cliente = new Cliente(); // novo registro: o banco gera o ID
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
        Optional<Cliente> clienteExistente = repository.findByCnpj(cliente.getCnpj());
        if (clienteExistente.isPresent()
                && !clienteExistente.get().getId().equals(cliente.getId())) {
            Notification.show("CNPJ já cadastrado para outro cliente!")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        try {
            Cliente salvo = service.salvarCliente(cliente);
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