package com.minitextil.gestor.app.view;

import com.minitextil.gestor.app.utils.ApplicationInfo;
import com.minitextil.gestor.components.core.program.Program;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class HomeView extends VerticalLayout implements Program {

    private static final long serialVersionUID = 1L;

    public HomeView(ApplicationInfo appInfo) {
        setSizeFull();
        setPadding(true);
        setAlignItems(Alignment.END);              // card no lado direito
        setJustifyContentMode(JustifyContentMode.START);

        add(criarCardBoasVindas(appInfo));
    }

    private Div criarCardBoasVindas(ApplicationInfo appInfo) {
        Span saudacao = new Span("Bem Vindo");
        saudacao.addClassName("home-card-saudacao");

        H3 usuario = new H3(obterNomeUsuario());
        usuario.addClassName("home-card-usuario");

        Div cabecalho = new Div(saudacao, usuario);
        cabecalho.addClassName("home-card-cabecalho");

        Span sistema = new Span("MiniTextil - Gestor");
        sistema.addClassName("home-card-sistema");

        Span versao = new Span("Versão: " + appInfo.getVersion());
        versao.addClassName("home-card-versao");

        Div rodape = new Div(sistema, versao);
        rodape.addClassName("home-card-rodape");

        Div card = new Div(cabecalho, rodape);
        card.addClassName("home-card");
        return card;
    }

    private String obterNomeUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return "Usuário";
        }
        return auth.getName(); // login do operador
    }

    @Override
    public Component getView() {
        return this;
    }
}