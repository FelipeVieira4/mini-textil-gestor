package com.minitextil.gestor.components.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.minitextil.gestor.operador.model.Operador;

@Component
public class SecurityUtils {

    public static Operador getOperadorLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.getPrincipal() instanceof Operador) {
            return (Operador) auth.getPrincipal();
        }
        
        return null;
    }
}