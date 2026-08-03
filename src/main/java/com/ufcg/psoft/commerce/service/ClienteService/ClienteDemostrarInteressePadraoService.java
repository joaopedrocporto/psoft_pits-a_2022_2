package com.ufcg.psoft.commerce.service.ClienteService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborDisponivelException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborIdInvalidoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborInexistenteException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ClienteDemostrarInteressePadraoService implements ClienteDemostrarInteresseService {
    @Autowired
    SaborRepository saborRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public Sabor interessar(Long idCliente, String codigoAcesso, Long idSabor)
            throws SaborDisponivelException,SaborIdInvalidoException, CodigoDeAcessoInvalidoException,
            SaborInexistenteException, ClienteIdInvalidoException, ClienteNaoExisteException
    {
        validarIdSabor(idSabor);
        Sabor saborProcurado = this.saborRepository.findById(idSabor).orElseThrow(SaborInexistenteException::new);

        if(saborProcurado.isDisponivel()) {
            throw new SaborDisponivelException();
        }

        validarId(idCliente);
        Cliente clienteInteressado = this.clienteRepository.findById(idCliente).orElseThrow(ClienteNaoExisteException::new);
        validarCodigoAcesso(clienteInteressado.getCodigoAcesso(),codigoAcesso);
        Set<Cliente> clientesInteressados = saborProcurado.getClientesInteressados();
        clientesInteressados.add(clienteInteressado);
        saborProcurado.setClientesInteressados(clientesInteressados);
        return this.saborRepository.save(saborProcurado);
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new ClienteIdInvalidoException();
    }
    private void validarIdSabor(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new SaborIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}
