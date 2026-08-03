package com.ufcg.psoft.commerce.service.ClienteService;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteResponseDTO;
import com.ufcg.psoft.commerce.model.Cliente;

@FunctionalInterface
public interface ClienteAlteraService {

    public ClienteResponseDTO alterar(Long id, ClientePostPutRequestDTO clienteAlterado , String codigoAcesso);

}
