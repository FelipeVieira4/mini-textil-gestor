package com.minitextil.gestor.genericos.view;

import com.minitextil.gestor.genericos.embeddable.Endereco;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class EnderecoComponent extends FormLayout {
    private static final long serialVersionUID = 1L;

    private final Binder<Endereco> binder = new BeanValidationBinder<>(Endereco.class);

    private final TextField cep = new TextField("CEP");
    private final TextField logradouro = new TextField("Logradouro");
    private final TextField numero = new TextField("Número");
    private final TextField complemento = new TextField("Complemento");
    private final TextField bairro = new TextField("Bairro");
    private final TextField cidade = new TextField("Cidade");
    private final ComboBox<String> uf = new ComboBox<>("UF");

    public EnderecoComponent() {
        // Configura uma grid responsiva de até 4 colunas
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("600px", 4)
        );

        // Linha 1: CEP (1), Logradouro (2), Número (1) = Total 4 colunas
        setColspan(cep, 1);
        setColspan(logradouro, 2);
        setColspan(numero, 1);

        // Linha 2: Complemento (1), Bairro (1), Cidade (1), UF (1) = Total 4 colunas
        setColspan(complemento, 1);
        setColspan(bairro, 1);
        setColspan(cidade, 1);
        setColspan(uf, 1);

        uf.setItems("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG",
                "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");

        // Configurações de Binder internas
        binder.forField(cep)
                .asRequired("Informe o CEP")
                .withConverter(
                        valor -> valor == null ? null : valor.replaceAll("\\D", ""),  // campo -> bean
                        valor -> valor                                                 // bean -> campo
                )
                .withValidator(v -> v == null || v.length() == 8, "CEP deve ter 8 dígitos")
                .bind(Endereco::getCep, Endereco::setCep);

        binder.forField(logradouro).asRequired("Informe o logradouro").bind(Endereco::getLogradouro, Endereco::setLogradouro);
        binder.forField(numero).asRequired("Informe o número").bind(Endereco::getNumero, Endereco::setNumero);
        binder.forField(complemento).bind(Endereco::getComplemento, Endereco::setComplemento);
        binder.forField(bairro).asRequired("Informe o bairro").bind(Endereco::getBairro, Endereco::setBairro);
        binder.forField(cidade).asRequired("Informe a cidade").bind(Endereco::getCidade, Endereco::setCidade);
        binder.forField(uf).asRequired("Informe a UF").bind(Endereco::getUf, Endereco::setUf);

        cep.setValueChangeMode(ValueChangeMode.ON_BLUR);
        cep.addValueChangeListener(event -> {
            String cepInformado = event.getValue();
            if (cepInformado != null && !cepInformado.isBlank()) {
                buscarCep(cepInformado.replaceAll("\\D", ""));
            }
        });

        add(cep, logradouro, numero, complemento, bairro, cidade, uf);
    }

    public boolean writeBeanIfValid(Endereco endereco) {
        return binder.writeBeanIfValid(endereco);
    }

    public void readBean(Endereco endereco) {
        binder.readBean(endereco);
    }

    public void clear() {
        binder.readBean(null);
    }

    private void buscarCep(String cepLimpo) {
        if (cepLimpo.length() != 8) return;
        try {
            String url = "https://viacep.com.br/ws/" + cepLimpo + "/json/";
            RestTemplate restTemplate = new RestTemplate();
            @SuppressWarnings("unchecked")
            Map<String, Object> resposta = restTemplate.getForObject(url, Map.class);

            if (resposta != null && Boolean.TRUE.equals(resposta.get("erro"))) {
                Notification.show("CEP não encontrado.").addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (resposta != null) {
                logradouro.setValue((String) resposta.getOrDefault("logradouro", ""));
                bairro.setValue((String) resposta.getOrDefault("bairro", ""));
                cidade.setValue((String) resposta.getOrDefault("localidade", ""));
                uf.setValue((String) resposta.getOrDefault("uf", ""));
                numero.focus();
            }
        } catch (Exception e) {
            Notification.show("Erro ao consultar o CEP.").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}