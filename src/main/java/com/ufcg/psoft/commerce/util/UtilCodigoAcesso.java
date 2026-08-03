package com.ufcg.psoft.commerce.util;

import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Estabelecimento;

public class UtilCodigoAcesso {
    public static boolean verificarConteudo(String codigo) {
        return FuncoesValidacao.isNotEmptyOrNull(codigo);
    }

    public static boolean validarTamanho(String codigo) {
        return FuncoesValidacao.isNotEmptyOrNull(codigo) && codigo.length() == 6;
    }

    public static boolean validarCodigo(String codigoEsperado, String codigoPassado) {
        return verificarConteudo(codigoEsperado) &&
                verificarConteudo(codigoPassado) &&
                validarTamanho(codigoEsperado) &&
                validarTamanho(codigoPassado) &&
                codigoEsperado.equals(codigoPassado);
    }
}