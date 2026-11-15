package com.minitextil.gestor.genericos.utils;

import org.springframework.stereotype.Service;

public final class CnpjUtils {
    public static String limpar(String valor) {
        return valor == null ? null : valor.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    public static boolean formatoValido(String limpo) {
        return limpo != null && limpo.matches("[A-Z0-9]{12}\\d{2}");
    }

    public static String formatar(String valor) {
        String v = limpar(valor);
        if (v == null || v.length() != 14) return valor;
        return v.substring(0, 2) + "." + v.substring(2, 5) + "." + v.substring(5, 8)
                + "/" + v.substring(8, 12) + "-" + v.substring(12);
    }
}