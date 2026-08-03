package com.ufcg.psoft.commerce.controller;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.model.Associacao;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.service.AssociacaoService.AssociacaoExibirService;
import com.ufcg.psoft.commerce.service.AssociacaoService.AssociacaoExibirTodosService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Testes do controlador de Associação")
class AssociacaoControllerTests {

    final String URI_ASSOCIACAO = "/associacao";

    @Autowired
    MockMvc driver;

    @Autowired
    AssociacaoRepository associacaoRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    AssociacaoExibirTodosService associacaoExibirTodosService;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    Entregador entregador;

    Estabelecimento estabelecimento;

    Estabelecimento estabelecimento2;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        // Object Mapper suporte para LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        entregador = entregadorRepository.save(Entregador.builder()
                .nome("Entregador Um")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Branco")
                .tipoVeiculo("Carro")
                .codigoAcesso("123456")
                .build()
        );
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654321")
                .build()
        );

        estabelecimento2 = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654231")
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        entregadorRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
        associacaoRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de criacao de associacao")
    class ClienteCriacaoAssociacao {

        @Test
        @DisplayName("Quando criamos uma associacao com sucesso")
        void testCriarAssociacaoComSucesso() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Associacao resultado = objectMapper.readValue(responseJsonString, Associacao.AssociacaoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(1, associacaoRepository.count()),
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(entregador.getId(), resultado.getEntregadorId()),
                    () -> assertEquals(estabelecimento.getId(), resultado.getEstabelecimentoId())
            );
        }

        @Test
        @DisplayName("Quando tentamos criar outra associacao igual")
        void testCriarAssociacaoIgualInvalida() throws Exception {
            // Arrange

            String responseJsonString1 = driver.perform(post(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            // Act
            String responseJsonString2 = driver.perform(post(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString2, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Essa associacao ja existe", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando criamos uma associacao com entregador inexistente")
        void testCriarAssociacaoComEntregadorInexistente() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", "9999")
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals(0, associacaoRepository.count()),
                    () -> assertEquals("O entregador consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando criamos uma associacao com estabelecimento inexistente")
        void testCriarAssociacaoComEstabelecimentoInexistente() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("estabelecimentoId", "9999"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals(0, associacaoRepository.count()),
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando criamos uma associacao passando codigo de acesso invalido")
        void testCriarAssociacaoComCodigoDeAcessoInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", "654321")
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals(0, associacaoRepository.count()),
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de aprovação de associacao")
    class ClienteAprovacaoAssociacao {

        @BeforeEach
        void setUp() {
            associacaoRepository.save(Associacao.builder()
                    .entregadorId(entregador.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .status(false)
                    .build()
            );
        }

        @Test
        @DisplayName("Quando aprovamos uma associacao com sucesso")
        void quandoAprovamosAssociacaoComSucesso() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(put(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("estabelecimentoId", estabelecimento.getId().toString())
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Associacao resultado = objectMapper.readValue(responseJsonString, Associacao.AssociacaoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(1, associacaoRepository.count()),
                    () -> assertTrue(resultado.isStatus())
            );
        }

        @Test
        @DisplayName("Quando aprovamos uma associacao com entregador inexistente")
        void quandoAprovamosAssociacaoComEntregadorInexistente() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(put(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", "9999")
                            .param("estabelecimentoId", estabelecimento.getId().toString())
                            .param("codigoAcesso", entregador.getCodigoAcesso()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals(1, associacaoRepository.count()),
                    () -> assertEquals("O entregador consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando aprovamos uma associacao com estabelecimento inexistente")
        void quandoAprovamosAssociacaoComEstabelecimentoInexistente() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(put(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("estabelecimentoId", "9999"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals(1, associacaoRepository.count()),
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando aprovamos uma associacao passando codigo de acesso invalido")
        void quandoAprovamosAssociacaoComCodigoDeAcessoInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_ASSOCIACAO)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("entregadorId", entregador.getId().toString())
                                .param("codigoAcesso", "654321")
                                .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals(1, associacaoRepository.count()),
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de aprovação de associacao")
    class ClienteAssociacaoPatch {

        @BeforeEach
        void setUp() {
            associacaoRepository.save(Associacao.builder()
                    .entregadorId(entregador.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .codigoAcesso(entregador.getCodigoAcesso())
                    .status(false)
                    .build()
            );
        }

        @Test
        @DisplayName("Quando um patch eh valido")
        void quandoAprovamosAssociacaoPatchValido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(patch(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Associacao resultado = objectMapper.readValue(responseJsonString, Associacao.AssociacaoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertTrue(resultado.isStatus())
            );
        }

        @Test
        @DisplayName("Quando um patch eh invalido")
        void associacaoPatchInvalido() throws Exception {
            // Arrange
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("111111")
                    .build();

            estabelecimentoRepository.save(estabelecimento1);
            // Act
            String responseJsonString = driver.perform(patch(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", estabelecimento1.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento1.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Associacao nao existe", resultado.getMessage())
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação get Associacao")
    class ClienteAssociacaoGet {

        @Test
        @DisplayName("Quando um get eh invalido")
        void associacaoGetAssociacaoInvalido() throws Exception {
            // Arrange
            Estabelecimento estabelecimento2 = Estabelecimento.builder()
                    .codigoAcesso("111111")
                    .build();

            estabelecimentoRepository.save(estabelecimento2);
            // Act
            String responseJsonString = driver.perform(get(URI_ASSOCIACAO + "/exibir")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", estabelecimento2.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento2.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Associacao nao existe", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando um get eh invalido")
        void associacaoGetCodigoInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(get(URI_ASSOCIACAO + "/exibir")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", "1")
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando um get eh invalido")
        void associacaoGetEntregadorInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(get(URI_ASSOCIACAO + "/exibir")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", "111111")
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O entregador consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando um get eh invalido")
        void associacaoGetEstabelecimentoIdInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(get(URI_ASSOCIACAO + "/exibir")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("estabelecimentoId", "-1"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Estabelecimento Id Invalido", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando um get eh invalido")
        void associacaoGetValido() throws Exception {
            // Arrange
            Entregador entregador3 = entregadorRepository.save(Entregador.builder()
                    .nome("Entregador Umm")
                    .placaVeiculo("ABC-3234")
                    .corVeiculo("Azuç")
                    .tipoVeiculo("Carro")
                    .codigoAcesso("122456")
                    .build()
            );

            associacaoRepository.save(Associacao.builder()
                    .entregadorId(entregador3.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .codigoAcesso(entregador3.getCodigoAcesso())
                    .status(false)
                    .build());

            // Act
            String responseJsonString = driver.perform(get(URI_ASSOCIACAO + "/exibir")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador3.getId().toString())
                            .param("codigoAcesso", entregador3.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Associacao resultado = objectMapper.readValue(responseJsonString, Associacao.AssociacaoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(resultado.getEntregadorId(), entregador3.getId()),
                    () -> assertEquals(resultado.getEstabelecimentoId(), estabelecimento.getId())
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação get All Associacao de um entregador")
    class ClienteAssociacaoGetAll {

        @Test
        @DisplayName("Quando um get All eh valido e vazio")
        void associacaoGetAllValido1() throws Exception {
            Entregador entregador4 = entregadorRepository.save(Entregador.builder()
                    .nome("Entregador Umm")
                    .placaVeiculo("ABC-3237")
                    .corVeiculo("Azuh")
                    .tipoVeiculo("Carro")
                    .codigoAcesso("122456")
                    .build()
            );
            // Act
            List<Associacao> associacoes = associacaoExibirTodosService
                    .exibirTodos(entregador4.getId(), entregador4.getCodigoAcesso());

            // Assert
            assertAll(
                    () -> assertNotNull(associacoes),
                    () -> assertEquals(new ArrayList<>(), associacoes)
            );
        }


        @Test
        @DisplayName("Quando um get All eh valido e nao vazio")
        void associacaoGetAllValido2() throws Exception {

            Entregador entregador5 = entregadorRepository.save(Entregador.builder()
                    .nome("Entregador 2")
                    .placaVeiculo("ABC-4237")
                    .corVeiculo("Azul")
                    .tipoVeiculo("Carro")
                    .codigoAcesso("127459")
                    .build()
            );

            associacaoRepository.save(Associacao.builder()
                    .entregadorId(entregador5.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .codigoAcesso(entregador5.getCodigoAcesso())
                    .status(false)
                    .build());

            // Act
            List<Associacao> associacoes = associacaoExibirTodosService
                    .exibirTodos(entregador5.getId(), entregador5.getCodigoAcesso());

            // Assert
            assertAll(
                    () -> assertNotNull(associacoes),
                    () -> assertEquals(associacaoRepository.retornarAssociacoes(entregador5.getId()), associacoes)
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação delete Associacao de um entregador e estabelecimento")
    class ClienteAssociacaoDelete {

        @Test
        @DisplayName("Quando um delete eh invalido")
        void associacaoDeleteCodigoInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(delete(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", "1")
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando um delete eh invalido codigo diferente")
        void associacaoDeleteCodigoIncompativelInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(delete(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", "101101")
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando um delete tem id invalido")
        void associacaoDeleteIdInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(delete(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("estabelecimentoId", "-1"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Estabelecimento Id Invalido", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando um delete eh invalido associacao nao existe")
        void associacaoDeleteInvalidoAssociacaoNaoExiste() throws Exception {
            // Arrange

            Entregador entregador6 = entregadorRepository.save(Entregador.builder()
                    .nome("Entregador e")
                    .placaVeiculo("ABC-0237")
                    .corVeiculo("Azui")
                    .tipoVeiculo("Carro")
                    .codigoAcesso("166456")
                    .build()
            );
            // Act
            String responseJsonString = driver.perform(delete(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador6.getId().toString())
                            .param("codigoAcesso", entregador6.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Associacao nao existe", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando um delete eh invalido entregador nao existe")
        void associacaoDeleteInvalidoEntregadorNaoExiste() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(delete(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", "88")
                            .param("codigoAcesso", "103021")
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O entregador consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando um estabelecimento eh deletado a associacao deve tambem ser")
        void associacaoDeletandoEstabelecimentoValido() throws Exception {
            // Arrange

            Entregador entregador7 = entregadorRepository.save(Entregador.builder()
                    .nome("Entregador eita")
                    .placaVeiculo("ABC-0937")
                    .corVeiculo("Azuioioi")
                    .tipoVeiculo("Carro")
                    .codigoAcesso("263456")
                    .build()
            );

            /**
             * Criando uma Associacao entre entregador7 e estabelecimento2
             */
            String responseJsonString1 = driver.perform(post(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador7.getId().toString())
                            .param("codigoAcesso", entregador7.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento2.getId().toString()))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Associacao resultado1 = objectMapper.readValue(responseJsonString1, Associacao.AssociacaoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado1),
                    () -> assertNotNull(resultado1.getId()),
                    () -> assertEquals(entregador7.getId(), resultado1.getEntregadorId()),
                    () -> assertEquals(estabelecimento2.getId(), resultado1.getEstabelecimentoId())
            );

            Long idAssociacao = resultado1.getId();

            /**
             * Buscando AssociacDeletadoao e fazendo validacoes
             */
            String responseJsonString2 = driver.perform(get(URI_ASSOCIACAO + "/exibir")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador7.getId().toString())
                            .param("codigoAcesso", entregador7.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento2.getId().toString()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Associacao resultado2 = objectMapper.readValue(responseJsonString2, Associacao.AssociacaoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(idAssociacao, resultado2.getId()),
                    () -> assertEquals(resultado2.getEntregadorId(), entregador7.getId()),
                    () -> assertEquals(resultado2.getEstabelecimentoId(), estabelecimento2.getId())
            );

            int qtdAssociacoesAntesDeExcluirEstabelecimento = associacaoRepository.findAll().size();

            /**
             * Excluindo estabelecimento, o que deve excluir a Associacao por consequencia
             */
            // Act
            String responseJsonString3 = driver.perform(delete("/estabelecimentos/" + estabelecimento2.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso",estabelecimento2.getCodigoAcesso()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            /**
             * Buscando Associacao apos excluir o estabelecimento
             */
            String responseJsonString4 = driver.perform(get(URI_ASSOCIACAO + "/exibir")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador7.getId().toString())
                            .param("codigoAcesso", entregador7.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento2.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado4 = objectMapper.readValue(responseJsonString4, CustomErrorType.class);



            // Assert
            assertAll(
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado4.getMessage()),
                    () -> assertTrue(qtdAssociacoesAntesDeExcluirEstabelecimento - 1 == associacaoRepository.findAll().size()),
                    () -> assertNull(associacaoRepository.retornarAssociacao(entregador7.getId(), estabelecimento2.getId()))
            );

        }

        @Test
        @DisplayName("Quando um entregador eh deletado a associacao deve tambem ser")
        void associacaoDeleteEntregadorValido() throws Exception {
            // Arrange
            Entregador entregador8 = entregadorRepository.save(Entregador.builder()
                    .nome("Entregador eita")
                    .placaVeiculo("ABC-0988")
                    .corVeiculo("Azuioioi888")
                    .tipoVeiculo("Carro")
                    .codigoAcesso("268486")
                    .build()
            );

            /**
             * Criando uma Associacao entre entregador8 e estabelecimento
             */
            String responseJsonString1 = driver.perform(post(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador8.getId().toString())
                            .param("codigoAcesso", entregador8.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Associacao resultado1 = objectMapper.readValue(responseJsonString1, Associacao.AssociacaoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado1),
                    () -> assertNotNull(resultado1.getId()),
                    () -> assertEquals(entregador8.getId(), resultado1.getEntregadorId()),
                    () -> assertEquals(estabelecimento.getId(), resultado1.getEstabelecimentoId())
            );

            Long idAssociacao = resultado1.getId();

            /**
             * Buscando Associacao e fazendo validacoes
             */
            String responseJsonString2 = driver.perform(get(URI_ASSOCIACAO + "/exibir")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador8.getId().toString())
                            .param("codigoAcesso", entregador8.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Associacao resultado2 = objectMapper.readValue(responseJsonString2, Associacao.AssociacaoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(idAssociacao, resultado2.getId()),
                    () -> assertEquals(resultado2.getEntregadorId(), entregador8.getId()),
                    () -> assertEquals(resultado2.getEstabelecimentoId(), estabelecimento.getId())
            );

            int qtdAssociacoesAntesDeExcluirEntregador = associacaoRepository.findAll().size();

            /**
             * Excluindo o entregador, o que deve excluir a Associacao por consequencia
             */
            // Act
            String responseJsonString3 = driver.perform(delete("/entregadores/" + entregador8.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso",entregador8.getCodigoAcesso()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            /**
             * Buscando Associacao apos excluir o estabelecimento
             */
            String responseJsonString4 = driver.perform(get(URI_ASSOCIACAO + "/exibir")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador8.getId().toString())
                            .param("codigoAcesso", entregador8.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado4 = objectMapper.readValue(responseJsonString4, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O entregador consultado nao existe!", resultado4.getMessage()),
                    () -> assertTrue(qtdAssociacoesAntesDeExcluirEntregador - 1 == associacaoRepository.findAll().size()),
                    () -> assertNull(associacaoRepository.retornarAssociacao(entregador8.getId(), estabelecimento2.getId()))
            );
        }

        @Test
        @DisplayName("Quando um delete eh valido")
        void associacaoDeleteValido() throws Exception {
            // Arrange

            Entregador entregador9 = entregadorRepository.save(Entregador.builder()
                    .nome("Entregador ei7ta")
                    .placaVeiculo("ABC-0987")
                    .corVeiculo("Azuioioi888")
                    .tipoVeiculo("Carro")
                    .codigoAcesso("267486")
                    .build()
            );
            associacaoRepository.save(Associacao.builder()
                    .codigoAcesso(entregador9.getCodigoAcesso())
                    .entregadorId(entregador9.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .status(false)
                    .build());
            // Act

            int qtdAssociacoesAntesDeExcluir = associacaoRepository.findAll().size();

            String responseJsonString = driver.perform(delete(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador9.getId().toString())
                            .param("codigoAcesso", entregador9.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertAll(
                    () -> assertTrue(qtdAssociacoesAntesDeExcluir - 1 == associacaoRepository.findAll().size()),
                    () -> assertNull(associacaoRepository.retornarAssociacao(entregador9.getId(), estabelecimento.getId()))
            );
        }

        @Test
        @DisplayName("Quando um delete por id eh valido")
        void associacaoDeletePorIdValido() throws Exception {
            // Arrange

            Entregador entregador10 = entregadorRepository.save(Entregador.builder()
                    .nome("Entregador ei75ta")
                    .placaVeiculo("ABC-5587")
                    .corVeiculo("Azuioioi8588")
                    .tipoVeiculo("Carro")
                    .codigoAcesso("254586")
                    .build()
            );

            Associacao associacao = associacaoRepository.save(Associacao.builder()
                    .codigoAcesso(entregador10.getCodigoAcesso())
                    .entregadorId(entregador10.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .status(false)
                    .build());

            int qtdAssociacoesAntesDeExcluir = associacaoRepository.findAll().size();

            // Act
            String responseJsonString = driver.perform(delete(URI_ASSOCIACAO + "/"+ associacao.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", entregador10.getCodigoAcesso()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertAll(
                    () -> assertTrue(qtdAssociacoesAntesDeExcluir - 1 == associacaoRepository.findAll().size()),
                    () -> assertNull(associacaoRepository.retornarAssociacao(entregador10.getId(), estabelecimento.getId()))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de associacao do entregado verificando a disponibilidade")
    class EntregadorDefinirDisponibilidade {

        @Test
        @DisplayName("Quando associamos um entregador disponivel")
        void quandoAssociamosEntregadorDisponivel() throws Exception {
            // Arrange
            entregador.setDisponibilidade(true);
            entregadorRepository.save(entregador);
            //Act
            String responseJsonString = driver.perform(post(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Associacao resultado = objectMapper.readValue(responseJsonString, Associacao.AssociacaoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(1, associacaoRepository.count()),
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(entregador.getId(), resultado.getEntregadorId()),
                    () -> assertEquals(estabelecimento.getId(), resultado.getEstabelecimentoId())
            );
        }

        @Test
        @DisplayName("Quando associamos um entregador indisponível")
        void quandoAssociamosEntregadorIndisponivel() throws Exception {
            // Arrange
            entregador.setDisponibilidade(false);
            entregadorRepository.save(entregador);
            //Act
            String responseJsonString2 = driver.perform(post(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString2, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Entregador indisponivel", resultado.getMessage())
            );
        }
    }
}
