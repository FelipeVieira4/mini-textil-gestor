package com.minitextil.gestor.natureza.view;

import com.minitextil.gestor.components.core.program.ProgramErp;
import com.minitextil.gestor.empresa.model.Empresa;
import com.minitextil.gestor.empresa.repository.EmpresaRepository;
import com.minitextil.gestor.empresa.view.LookupEmpresa;
import com.minitextil.gestor.natureza.model.Natureza;
import com.minitextil.gestor.natureza.model.NaturezaId;
import com.minitextil.gestor.natureza.respository.NaturezaRepository;
import com.minitextil.gestor.natureza.service.NaturezaService;
import com.minitextil.gestor.operador.model.UsuarioSessao;
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

import java.util.Optional;

public class CadastroNatureza extends ProgramErp {
    private static final long serialVersionUID = 1L;

    private final Binder<Natureza> binder = new BeanValidationBinder<>(Natureza.class);
    private final NaturezaRepository repository;
    private final NaturezaService service;
    private final EmpresaRepository empresaRepository;
    private final UsuarioSessao usuarioSessao;

    // Chave composta (não passam pelo binder, são montadas manualmente no NaturezaId)
    private final IntegerField codEmpresa = new IntegerField("Código Empresa");
    private final Button btObterEmpresa = new Button(new Icon(VaadinIcon.SEARCH));
    private final TextField descricaoEmpresa = new TextField("Empresa");
    private final IntegerField codNatureza = new IntegerField("Código Natureza");
    private final Button btObterNatureza = new Button(new Icon(VaadinIcon.SEARCH));

    // Campos ligados ao binder
    private final TextField descricao = new TextField("Descrição");
    private final Checkbox ativo = new Checkbox("Ativo");

    private Natureza naturezaEmEdicao;

    public CadastroNatureza(NaturezaRepository repository,
                            NaturezaService service,
                            EmpresaRepository empresaRepository,
                            UsuarioSessao usuarioSessao) {
        this.repository = repository;
        this.service = service;
        this.empresaRepository = empresaRepository;
        this.usuarioSessao = usuarioSessao;

        this.setWidthFull();
        this.setAlignItems(Alignment.CENTER);

        setCabecalho("Cadastro Natureza");

        // ----- Empresa (código + lookup + descrição somente leitura) -----
        HorizontalLayout empresaCampo = new HorizontalLayout(codEmpresa, btObterEmpresa);
        empresaCampo.setDefaultVerticalComponentAlignment(HorizontalLayout.Alignment.BASELINE);
        empresaCampo.setWidthFull();

        descricaoEmpresa.setReadOnly(true);

        HorizontalLayout naturezaCampo = new HorizontalLayout(codNatureza, btObterNatureza);
        naturezaCampo.setDefaultVerticalComponentAlignment(HorizontalLayout.Alignment.BASELINE);
        naturezaCampo.setWidthFull();

        // ----- Descrição / Situação -----
        descricao.setMaxLength(75);

        binder.forField(descricao)
                .asRequired("Informe a descrição")
                .bind(Natureza::getDescricao, Natureza::setDescricao);

        binder.forField(ativo)
                .bind(Natureza::getAtivo, Natureza::setAtivo);

        // ----- Listeners -----
        codEmpresa.setValueChangeMode(ValueChangeMode.ON_BLUR);
        codEmpresa.addValueChangeListener(event -> {
            atualizarDescricaoEmpresa(event.getValue());
            tentarCarregar();
        });

        codNatureza.setValueChangeMode(ValueChangeMode.ON_BLUR);
        codNatureza.setHelperText("Deixe em branco para gerar o próximo código");
        codNatureza.addValueChangeListener(_ -> tentarCarregar());

        btObterEmpresa.addClickListener(_ -> {
            LookupEmpresa lookup = new LookupEmpresa();
            lookup.VerTodasEmpresas(this.empresaRepository, empresaSelecionada ->
                    this.codEmpresa.setValue(empresaSelecionada.getId().intValue()));
        });

        btObterNatureza.addClickListener(_ -> {
            Integer emp = this.codEmpresa.getValue();
            if (emp == null) {
                Notification.show("Informe a empresa antes de pesquisar a natureza!")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            LookupNatureza lookup = new LookupNatureza();
            lookup.VerTodasNatureza(this.repository, naturezaSelecionada ->
                            this.codNatureza.setValue(naturezaSelecionada.getId().getCodNatureza().intValue()),
                    emp.longValue());
        });

        Button btSalvar = new Button("Salvar", _ -> salvar());
        btSalvar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("50vw");
        formLayout.setRowSpacing("25px");

        formLayout.add(empresaCampo, descricaoEmpresa,
                naturezaCampo, new Div(),
                descricao, new Div(),
                ativo, new Div(),
                btSalvar);

        add(criarCard(formLayout));
        limparDados();
        sugerirEmpresaAtiva();
    }

    /** Sugere a empresa ativa da sessão (o usuário ainda pode trocar). */
    private void sugerirEmpresaAtiva() {
        Empresa ativa = usuarioSessao.getEmpresaAtiva();
        if (ativa != null && ativa.getId() != null) {
            codEmpresa.setValue(ativa.getId().intValue());
        }
    }

    private void atualizarDescricaoEmpresa(Integer codigo) {
        if (codigo == null) {
            descricaoEmpresa.clear();
            return;
        }
        Optional<Empresa> empresaOpt = empresaRepository.findById(codigo.longValue());
        if (empresaOpt.isPresent()) {
            descricaoEmpresa.setValue(empresaOpt.get().getDescricao());
        } else {
            descricaoEmpresa.clear();
            Notification.show("Empresa " + codigo + " não encontrada!", 4000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /** Só busca quando os dois códigos da chave estão preenchidos. */
    private void tentarCarregar() {
        Integer emp = codEmpresa.getValue();
        Integer nat = codNatureza.getValue();

        if (emp == null || nat == null) {
            limparDados();
            return;
        }
        obterDadosNatureza(new NaturezaId(emp.longValue(), nat.longValue()));
    }

    private void obterDadosNatureza(NaturezaId chave) {
        Optional<Natureza> naturezaOpt = repository.findById(chave);

        if (naturezaOpt.isPresent()) {
            naturezaEmEdicao = naturezaOpt.get();
            binder.readBean(naturezaEmEdicao);
            Notification.show("Natureza (" + chave.getCodEmpresa() + "/" + chave.getCodNatureza()
                    + ") encontrada! Modo de edição ativo.");
        } else {
            limparDados();
            Notification.show("Natureza não encontrada. Um novo registro será criado ao salvar.",
                    4000, Notification.Position.MIDDLE);
        }
    }

    private void salvar() {
        Integer emp = codEmpresa.getValue();

        if (emp == null) {
            Notification.show("Informe a empresa!")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        if (empresaRepository.findById(emp.longValue()).isEmpty()) {
            Notification.show("Empresa " + emp + " não encontrada!")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        boolean isEdicao = naturezaEmEdicao != null;
        Natureza natureza = isEdicao ? naturezaEmEdicao : new Natureza();

        if (!binder.writeBeanIfValid(natureza)) {
            Notification.show("Preencha os campos obrigatórios corretamente.")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        // Novo registro: monta a chave (gera o próximo código se não foi informado)
        if (!isEdicao) {
            Long codNat = codNatureza.getValue() != null
                    ? codNatureza.getValue().longValue()
                    : repository.proximoCodigo(emp.longValue());

            natureza.setId(new NaturezaId(emp.longValue(), codNat));
        }

        try {
            Natureza salva = service.salvar(natureza);
            Notification.show("Natureza salva com sucesso! Código: " + salva.getId().getCodNatureza())
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            limparTudo();
        } catch (Exception e) {
            Notification.show("Erro ao salvar: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /** Limpa apenas os dados da natureza, mantendo os códigos digitados. */
    private void limparDados() {
        naturezaEmEdicao = null;
        binder.readBean(null);
        ativo.setValue(true);
    }

    /** Limpa os dados e o código da natureza, mantendo a empresa para o próximo cadastro. */
    private void limparTudo() {
        limparDados();
        codNatureza.clear();
    }

    @Override
    public Component getView() {
        return this;
    }
}