package com.minitextil.gestor.genericos.utils;

public final class TelefoneUtils {

    private TelefoneUtils() {}

    public static String limpar(String valor) {
        if (valor == null) return null;
        String valorLimpo = valor.replaceAll("\\D", "");
        return valorLimpo.isEmpty() ? null : valorLimpo;
    }

    public static boolean formatoValido(String limpo) {
        return limpo == null || limpo.matches("\\d{10,11}");
    }

    public static String formatar(String valor) {
        String valorLimpo = limpar(valor);
        if (valorLimpo == null) return "";            // antes: null
        if (valorLimpo.length() == 11) {
            return "(" + valorLimpo.substring(0, 2) + ") " + valorLimpo.substring(2, 7) + "-" + valorLimpo.substring(7);
        }
        if (valorLimpo.length() == 10) {
            return "(" + valorLimpo.substring(0, 2) + ") " + valorLimpo.substring(2, 6) + "-" + valorLimpo.substring(6);
        }
        return valor;
    }
}