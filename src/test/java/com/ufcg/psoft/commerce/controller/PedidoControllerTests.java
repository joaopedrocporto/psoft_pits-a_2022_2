package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoGetRequestDTO;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de pedidos")
public class PedidoControllerTests {
    final String URI_PEDIDOS = "/pedidos";

    @Autowired
    MockMvc driver;

    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    SaborRepository saborRepository;
    @Autowired
    PizzaRepository pizzaRepository;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    AssociacaoRepository associacaoRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    Cliente cliente;
    Entregador entregador;
    Entregador entregadorA;
    Sabor sabor1;
    Sabor sabor2;
    Pizza pizzaM;
    Pizza pizzaG;
    Estabelecimento estabelecimento;
    Pedido pedido;
    Pedido pedido1;
    Pedido pedido2;
    PedidoPostPutRequestDTO pedidoPostPutRequestDTO;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654321")
                .build());
        sabor1 = saborRepository.save(Sabor.builder()
                .nome("Sabor Um")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(true)
                .build());
        sabor2 = saborRepository.save(Sabor.builder()
                .nome("Sabor Dois")
                .tipo("doce")
                .precoM(15.0)
                .precoG(30.0)
                .disponivel(true)
                .build());
        cliente = clienteRepository.save(Cliente.builder()
                .nome("Anton Ego")
                .endereco("Paris")
                .codigoAcesso("123456")
                .build());
        entregador = entregadorRepository.save(Entregador.builder()
                .nome("Joãozinho")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("101010")
                .build());
        entregadorA = entregadorRepository.save(Entregador.builder()
                .nome("Matheuzinho")
                .placaVeiculo("ABC-4321")
                .corVeiculo("Preto")
                .tipoVeiculo("Moto")
                .codigoAcesso("101011")
                .build());
        pizzaM = Pizza.builder()
                .sabor1(sabor1.getId())
                .tamanho("media")
                .build();
        pizzaG = Pizza.builder()
                .sabor1(sabor1.getId())
                .sabor2(sabor2.getId())
                .tamanho("grande")
                .build();
        List<Pizza> pizzas = new ArrayList<>(List.of(pizzaM));
        List<Pizza> pizzas1 = new ArrayList<>(List.of(pizzaM, pizzaG));
        pedido = Pedido.builder()
                .preco(10.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .entregadorId(entregador.getId())
                .pizzas(pizzas)
                .build();
        pedido1 = pedidoRepository.save(Pedido.builder()
                .preco(10.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .entregadorId(entregador.getId())
                .pizzas(pizzas1)
                .build());
        pedido2 = Pedido.builder()
                .preco(10.0)
                .enderecoEntrega("Casa 240")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .entregadorId(entregador.getId())
                .pizzas(pizzas1)
                .build();
        pedidoPostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega(pedido.getEnderecoEntrega())
                .pizzas(pedido.getPizzas())
                .build();
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
        pedidoRepository.deleteAll();
        saborRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class PedidoVerificacaoFluxosBasicosApiRest {

        @Test
        @DisplayName("Quando criamos um novo pedido com dados válidos")
        void quandoCriamosUmNovoPedidoComDadosValidos() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteId", cliente.getId().toString())
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())// Codigo 201
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.PedidoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
                    () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                    () -> assertEquals(pedido.getPreco(), resultado.getPreco())
            );
        }

        @Test
        @DisplayName("Quando alteramos um novo pedido com dados válidos")
        void quandoAlteramosPedidoValido() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            Long pedidoId = pedido.getId();

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("pedidoId", pedido.getId().toString())
                            .param("codigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.PedidoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(pedidoId, resultado.getId().longValue()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
                    () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                    () -> assertEquals(pedido.getPreco(), resultado.getPreco())
            );
        }

        @Test
        @DisplayName("Quando alteramos um pedido inexistente")
        void quandoAlteramosPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("pedidoId", "999999")
                            .param("codigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos um pedido passando codigo de acesso invalido")
        void quandoAlteramosPedidoPassandoCodigoAcessoInvalido() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("pedidoId", pedido.getId().toString())
                            .param("codigoAcesso", "999999")
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca por todos seus pedidos salvos")
        void quandoClienteBuscaTodosPedidos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(pedido1);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + cliente.getId())
                            .param("codigoAcesso", cliente.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pedido> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(2, resultado.size());
        }

        @Test
        @DisplayName("Quando um cliente busca por um pedido seu salvo pelo id primeiro")
        void quandoClienteBuscaPedidoPorId() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId() + "/" + cliente.getId())
                            .param("codigoAcesso", cliente.getCodigoAcesso().toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pedido> listaResultados = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            Pedido resultado = listaResultados.get(0);

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
                    () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                    () -> assertEquals(pedido.getPreco(), resultado.getPreco())
            );
        }

        @Test
        @DisplayName("Quando um cliente busca por um pedido seu salvo por id inexistente")
        void quandoClienteBuscaPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + "999999" + "/" + cliente.getId())
                            .param("codigoAcesso", cliente.getCodigoAcesso().toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca por um pedido feito por outro cliente")
        void quandoClienteBuscaPedidoDeOutroCliente() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            Cliente cliente1 = clienteRepository.save(Cliente.builder()
                    .nome("Catarina")
                    .endereco("Casinha")
                    .codigoAcesso("121212")
                    .build());

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId() + "/" + cliente1.getId())
                            .param("codigoAcesso", cliente.getCodigoAcesso().toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }


        @Test
        @DisplayName("Quando um estabelecimento busca todos os pedidos feitos nele")
        void quandoEstabelecimentoBuscaTodosPedidos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(pedido1);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/estabelecimento/" + estabelecimento.getId())
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pedido> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(2, resultado.size());
        }

        @Test
        @DisplayName("Quando um estabelecimento busca por um pedido feito nele salvo pelo id primeiro")
        void quandoEstabelecimentoBuscaPedidoPorId() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/estabelecimento/" + pedido.getId() + "/" + estabelecimento.getId())
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pedido> listaResultados = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            Pedido resultado = listaResultados.get(0);

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                    () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
                    () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                    () -> assertEquals(pedido.getPreco(), resultado.getPreco())
            );
        }

        @Test
        @DisplayName("Quando um estabelecimento busca por um pedido feito nele salvo pelo id inexistente")
        void quandoEstabelecimentoBuscaPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/estabelecimento/" + estabelecimento.getCodigoAcesso() + "/" + estabelecimento.getId())
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um estabelecimento busca por um pedido feito em outro estabelecimento")
        void quandoEstabelecimentoBuscaPedidoDeOutroEstabelecimento() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            Estabelecimento estabelecimento1 = estabelecimentoRepository.save(Estabelecimento.builder()
                    .codigoAcesso("121212")
                    .build());

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/estabelecimento/" + pedido.getId() + "/" + estabelecimento1.getId())
                            .param("codigoAcesso", "131313")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente excluí um pedido não entregue")
        void quandoClienteExcluiPedidoNaoEntregue() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId() + "/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido não foi entregue ainda!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente excluí um pedido inexistente")
        void quandoClienteExcluiPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + "999999" + "/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente excluí todos seus pedidos feitos por ele salvos")
        void quandoClienteExcluiTodosPedidosSalvos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(Pedido.builder()
                    .preco(10.0)
                    .enderecoEntrega("Casa 237")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .pizzas(new ArrayList<>(List.of(pizzaM, pizzaG)))
                    .build());

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();


            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando um estabelencimento excluí um pedido feito nele salvo")
        void quandoEstabelecimentoExcluiPedidoSalvo() throws Exception {
            // Arrange
            pedido.setStatusEntrega("Pedido entregue");
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/estabelecimento/" + pedido.getId() + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando um estabelencimento excluí um pedido inexistente")
        void quandoEstabelecimentoExcluiPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/estabelecimento/" + "999999" + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um estabelencimento excluí um pedido feito em outro estabelecimento")
        void quandoEstabelecimentoExcluiPedidoDeOutroEstabelecimento() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            Estabelecimento estabelecimento1 = estabelecimentoRepository.save(Estabelecimento.builder()
                    .codigoAcesso("121212")
                    .build());

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/estabelecimento/" + pedido.getId() + "/" + estabelecimento1.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento1.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um estabelencimento excluí todos os pedidos feitos nele salvos")
        void quandoEstabelecimentoExcluiTodosPedidosSalvos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(Pedido.builder()
                    .preco(10.0)
                    .enderecoEntrega("Casa 237")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .pizzas(new ArrayList<>(List.of(pizzaM, pizzaG)))
                    .build());

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/estabelecimento/" + estabelecimento.getId())
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando um cliente cancela um pedido")
        void quandoClienteCancelaPedido() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId() + "/cancelar-pedido")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento")
        void quandoClienteBuscaPedidoFeitoEmEstabelecimento() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + "pedido-cliente-estabelecimento" + "/" + cliente.getId() + "/" + estabelecimento.getId() + "/" + pedido.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))


                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<PedidoGetRequestDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(1, resultado.size());
            assertEquals(pedido.getId(), resultado.get(0).getId());
            assertEquals(pedido.getClienteId(), resultado.get(0).getClienteId());
            assertEquals(pedido.getEstabelecimentoId(), resultado.get(0).getEstabelecimentoId());
        }

        @Test
        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento inexistente")
        void quandoClienteBuscaPedidoFeitoEmEstabelecimentoInexistente() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + "pedido-cliente-estabelecimento" + "/" + cliente.getId() + "/" + "999999" + "/" + pedido.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento com pedido inexistente")
        void quandoClienteBuscaPedidoFeitoEmEstabelecimentoComPedidoInexistente() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + "pedido-cliente-estabelecimento" + "/" + cliente.getId() + "/" + estabelecimento.getId() + "/" + "999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento com cliente inexistente")
        void quandoClienteBuscaPedidoFeitoEmEstabelecimentoComClienteInexistente() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + "pedido-cliente-estabelecimento" + "/" + "999999" + "/" + estabelecimento.getId() + "/" + pedido.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O cliente consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento com pedidoId null")
        void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComPedidoIdNull() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/pedidos-cliente-estabelecimento/" + cliente.getId() + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<PedidoGetRequestDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(2, resultado.size());
        }

        @Test
        @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento com status")
        void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComStatus() throws Exception {
            // Arrange
            Pedido pedido3 = pedidoRepository.save(Pedido.builder()
                    .preco(30.0)
                    .enderecoEntrega("Casa 237")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .pizzas(new ArrayList<>(List.of(pizzaM)))
                    .statusEntrega("Pedido em preparo")
                    .build());


            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/pedidos-cliente-estabelecimento/" + cliente.getId() + "/" + estabelecimento.getId() + "/" + pedido3.getStatusEntrega())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<PedidoGetRequestDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(1, resultado.size());
            assertEquals(pedido3.getId(), resultado.get(0).getId());
            assertEquals(pedido3.getClienteId(), resultado.get(0).getClienteId());
            assertEquals(pedido3.getEstabelecimentoId(), resultado.get(0).getEstabelecimentoId());
        }

        @Test
        @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento filtrados por entrega")
        void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComPedidosFiltradosPorEntrega() throws Exception {
            // Arrange
            Pedido pedido3 = pedidoRepository.save(Pedido.builder()
                    .preco(30.0)
                    .enderecoEntrega("Casa 237")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .pizzas(new ArrayList<>(List.of(pizzaM)))
                    .statusEntrega("Pedido entregue")
                    .build());
            Pedido pedido4 = pedidoRepository.save(Pedido.builder()
                    .preco(30.0)
                    .enderecoEntrega("Casa 237")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .pizzas(new ArrayList<>(List.of(pizzaM)))
                    .statusEntrega("Pedido em preparo")
                    .build());

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/pedidos-cliente-estabelecimento/" + cliente.getId() + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<PedidoGetRequestDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(3, resultado.size());
            assertEquals(pedido4.getId(), resultado.get(0).getId());
            assertEquals(pedido4.getClienteId(), resultado.get(0).getClienteId());
            assertEquals(pedido4.getEstabelecimentoId(), resultado.get(0).getEstabelecimentoId());
        }


    }

    @Nested
    @DisplayName("Alteração de estado de pedido")
    public class AlteracaoEstadoPedidoTest {
        Pedido pedido1;

        @BeforeEach
        void setUp() {
            pedido1 = pedidoRepository.save(Pedido.builder()
                    .estabelecimentoId(estabelecimento.getId())
                    .clienteId(cliente.getId())
                    .enderecoEntrega("Rua 1")
                    .pizzas(new ArrayList<>(List.of(pizzaG)))
                    .preco(10.0)
                    .build()
            );
        }

        @Test
        @DisplayName("Quando o pedido é criado")
        void quandoPedidoCriado() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + "pedido-cliente-estabelecimento" + "/" + cliente.getId() + "/" + estabelecimento.getId() + "/" + pedido.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))


                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<PedidoGetRequestDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(1, resultado.size());
            assertEquals(resultado.get(0).getStatusEntrega(), "Pedido recebido");
        }
    }

    @Test
    @DisplayName("Quando o cliente confirma o pagamento do pedido")
    void quandoClienteConfirmaPagamentoPedido() throws Exception {
        // Arrange
        // Act
        String responseJsonString = driver.perform(put(URI_PEDIDOS + "/confirmar-pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pedidoId", pedido1.getId().toString())
                        .param("metodoPagamento", "PIX")
                        .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                        .content(""))
                .andExpect(status().isOk()) // Codigo 200
                .andReturn().getResponse().getContentAsString();
        // Assert
        Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);
        assertEquals(resultado.getStatusEntrega(), "Pedido em preparo");
    }


    @Test
    @DisplayName("Quando o estabelecimento associa um pedido a um entregador")
    void quandoEstabelecimentoAssociaPedidoEntregador() throws Exception {
        //Arrange
        pedido.setStatusEntrega("Pedido pronto");
        pedidoRepository.save(pedido);
        List<Entregador> entregadores = new LinkedList<>();
        entregadores.add(entregador);
        estabelecimento.setEntregadoresDisponiveis(entregadores);
        entregador.setDisponibilidade(true);

        // Act
        String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId() + "/" + "/associar-pedido-entregador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pedidoId", pedido.getId().toString())
                        .param("estabelecimentoId", estabelecimento.getId().toString())
                        .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                        .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

        // Assert
        assertEquals(resultado.getStatusEntrega(), "Pedido em rota");
        assertEquals(entregador.getId(), resultado.getEntregadorId());
    }

    @Test
    @DisplayName("Quando o cliente confirma a entrega de um pedido")
    void quandoClienteConfirmaEntregaPedido() throws Exception {
        // Arrange
        pedido.setStatusEntrega("Pedido em rota");
        pedidoRepository.save(pedido);

        // Act
        String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId() + "/" + cliente.getId() + "/cliente-confirmar-entrega")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                        .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

        // Assert
        assertEquals(resultado.getStatusEntrega(), "Pedido entregue");
    }

    @Test
    @DisplayName("Quando o estabelecimento associa um pedido a um entregador deve ser repeitado a ordem de disponibildiade")
    void quandoEstabelecimentoAssociaPedidoEntregadorOrdem() throws Exception {
        //Arrange
        pedido.setStatusEntrega("Pedido pronto");
        pedidoRepository.save(pedido);
        List<Entregador> entregadores = new LinkedList<>();
        entregadores.add(entregador);
        entregadores.add(entregadorA);
        estabelecimento.setEntregadoresDisponiveis(entregadores);
        entregador.setDisponibilidade(true);

        // Act
        String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId() + "/" + "/associar-pedido-entregador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pedidoId", pedido.getId().toString())
                        .param("estabelecimentoId", estabelecimento.getId().toString())
                        .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                        .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

        // Assert
        assertEquals(resultado.getEntregadorId(), entregador.getId());
    }

    @Test
    @DisplayName("Quando o cliente confirma a entrega de um pedido com codigo de acesso errado")
    void quandoClienteConfirmaEntregaPedidoComCodigoErrado() throws Exception {
        // Arrange
        pedido.setStatusEntrega("Pedido em rota");
        pedido = pedidoRepository.save(pedido);

        // Act
        String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId() + "/" + cliente.getId() + "/cliente-confirmar-entrega")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("clienteCodigoAcesso", "999999"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        // Assert: o pedido nao pode ter sido marcado como entregue
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage()),
                () -> assertEquals("Pedido em rota",
                        pedidoRepository.findById(pedido.getId()).orElseThrow().getStatusEntrega())
        );
    }

    @Test
    @DisplayName("Quando o estabelecimento associa um pedido com um entregador mas não tem nenhum disponível. Assim que um entregador ficar disponivel tal pedido deve ser associado a ele.")
    void quandoEstabelecimentoAssociaPedidoEntregadorSemEntregadosDiponivelFluxo() throws Exception {
        //Arrange
        // pedido pronto e ainda sem entregador, estabelecimento sem nenhum disponivel
        pedido.setStatusEntrega("Pedido pronto");
        pedido.setEntregadorId(null);
        pedido = pedidoRepository.save(pedido);

        estabelecimento.setEntregadoresDisponiveis(new LinkedList<>());
        estabelecimento.addPedido(pedido);
        estabelecimento = estabelecimentoRepository.save(estabelecimento);

        entregador.setDisponibilidade(false);
        entregador = entregadorRepository.save(entregador);

        // associacao aprovada entre o entregador e o estabelecimento
        associacaoRepository.save(Associacao.builder()
                .entregadorId(entregador.getId())
                .estabelecimentoId(estabelecimento.getId())
                .codigoAcesso(entregador.getCodigoAcesso())
                .status(true)
                .build());

        // Act 1: sem entregador disponivel a associacao do pedido deve ser recusada
        driver.perform(put(URI_PEDIDOS + "/" + pedido.getId() + "/associar-pedido-entregador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("estabelecimentoId", estabelecimento.getId().toString())
                        .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
                .andExpect(status().isBadRequest())
                .andDo(print());

        assertNull(pedidoRepository.findById(pedido.getId()).orElseThrow().getEntregadorId());

        // Act 2: assim que o entregador fica disponivel o pedido pendente deve ser atribuido a ele
        driver.perform(put("/entregadores/" + entregador.getId() + "/disponibilidade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("codigoAcesso", entregador.getCodigoAcesso())
                        .param("disponibilidade", "true"))
                .andExpect(status().isOk())
                .andDo(print());

        // Assert
        Pedido pedidoAtualizado = pedidoRepository.findById(pedido.getId()).orElseThrow();
        assertAll(
                () -> assertEquals(entregador.getId(), pedidoAtualizado.getEntregadorId()),
                () -> assertEquals("Pedido em rota", pedidoAtualizado.getStatusEntrega())
        );
    }

    @Nested
    @DisplayName("Conjunto de casos de teste da confirmação de pagamento de um pedido")
    public class PedidoConfirmarPagamentoTests {

        Pedido pedido1;

        @BeforeEach
        void setUp() {
            pedido1 = pedidoRepository.save(Pedido.builder()
                    .estabelecimentoId(estabelecimento.getId())
                    .clienteId(cliente.getId())
                    .enderecoEntrega("Rua 1")
                   .pizzas(new ArrayList<>(List.of(pizzaG)))
                    .preco(10.0)
                    .build()
           );
        }

        @Test
        @DisplayName("Quando confirmamos o pagamento de um pedido por cartão de credito")
        void confirmaPagamentoCartaoCredito() throws Exception {
            // Arrange
            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/confirmar-pagamento")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                            .param("pedidoId", pedido1.getId().toString())
                            .param("metodoPagamento", "CREDITO")
                            .content(""))
                    .andExpect(status().isOk()) // Codigo 200
                    .andReturn().getResponse().getContentAsString();
            // Assert
            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);
            assertAll(
                    () -> assertTrue(resultado.getStatusPagamento()),
                    () -> assertEquals(10, resultado.getPreco())
            );
        }

       @Test
        @DisplayName("Quando confirmamos o pagamento de um pedido por cartão de debito")
        void confirmaPagamentoCartaoDebito() throws Exception {
            // Arrange
            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/confirmar-pagamento")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                            .param("pedidoId", pedido1.getId().toString())
                            .param("metodoPagamento", "DEBITO")
                            .content(""))
                    .andExpect(status().isOk()) // Codigo 200
                    .andReturn().getResponse().getContentAsString();
            // Assert
            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);
            assertAll(
                    () -> assertTrue(resultado.getStatusPagamento()),
                    () -> assertEquals(9.75, resultado.getPreco())
            );
        }

        @Test
        @DisplayName("Quando confirmamos o pagamento de um pedido por pix")
        void confirmaPagamentoPIX() throws Exception {
            // Arrange
            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/confirmar-pagamento")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("pedidoId", pedido1.getId().toString())
                            .param("metodoPagamento", "PIX")
                            .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                            .content(""))
                    .andExpect(status().isOk()) // Codigo 200
                    .andReturn().getResponse().getContentAsString();
            // Assert
            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);
            assertAll(
                    () -> assertTrue(resultado.getStatusPagamento()),
                    () -> assertEquals(9.5, resultado.getPreco())
            );
        }
    }


}
