package com.minitextil.gestor;

import com.minitextil.gestor.components.properties.OperadorAdminProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet("themes/gestor/styles.css")
@EnableConfigurationProperties(OperadorAdminProperties.class)
public class GestorApplication implements AppShellConfigurator {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
        SpringApplication.run(GestorApplication.class, args);
    }
}