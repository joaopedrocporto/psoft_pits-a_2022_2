package com.ufcg.psoft.commerce.util;

import com.ufcg.psoft.commerce.model.Cliente;

import java.util.List;

public class utilCliente {

    public static boolean verificarUsoEndereco(List<Cliente> clientes, String endereco) {
        if (clientes.isEmpty()) return false;
        for (Cliente cliente : clientes) {
            if (cliente.getEndereco().equals(endereco)) return true;
        }
        return false;
    }

}