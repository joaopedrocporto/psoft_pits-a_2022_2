package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.EstabelecimentoDTO.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.EstabelecimentoDTO.EstabelecimentoResponseDTO;
import com.ufcg.psoft.commerce.dto.SaborDTO.SaborResponseDTO;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Testes do controlador de estabelecimentos")
public class EstabelecimentoControllerTests {
    final String URI_ESTABELECIMENTOS = "/estabelecimentos";
    @Autowired
    MockMvc driver;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    SaborRepository saborRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Estabelecimento estabelecimento;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("123456")
                .build());
    }

    @AfterEach
    void tearDown() {
        estabelecimentoRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class EstabelecimentoVerificacaoFluxosBasicosApiRest {
        EstabelecimentoPostPutRequestDTO estabelecimentoPutRequestDTO;
        EstabelecimentoPostPutRequestDTO estabelecimentoPostRequestDTO;

        @BeforeEach
        void setup() {
            estabelecimentoPutRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("123456")
                    .build();
            estabelecimentoPostRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("654321")
                    .build();
        }

        @Test
        @DisplayName("Quando criamos um novo estabelecimento com dados válidos")
        void quandoCriarEstabelecimentoValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_ESTABELECIMENTOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimentoPostRequestDTO.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EstabelecimentoResponseDTO resultado = objectMapper.readValue(responseJsonString, EstabelecimentoResponseDTO.EstabelecimentoResponseDTOBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    // o codigo de acesso e segredo: nao pode voltar na resposta
                    () -> assertFalse(responseJsonString.contains("codigoAcesso")),
                    () -> assertEquals(
                            estabelecimentoPostRequestDTO.getCodigoAcesso(),
                            estabelecimentoRepository.findById(resultado.getId()).orElseThrow().getCodigoAcesso())
            );
        }

        @Test
        @DisplayName("Quando excluímos um estabelecimento salvo")
        void quandoExcluimosEstabelecimentoValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isNoContent()) // Codigo 204
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando atualizamos um estabelecimento salvo")
        void quandoAtualizamosEstabelecimentoValido() throws Exception {
            // Arrange
            estabelecimentoPutRequestDTO.setCodigoAcesso("131289");

            // Act
            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EstabelecimentoResponseDTO resultado = objectMapper.readValue(responseJsonString, EstabelecimentoResponseDTO.EstabelecimentoResponseDTOBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(resultado.getId().longValue(), estabelecimento.getId().longValue()),
                    () -> assertFalse(responseJsonString.contains("codigoAcesso")),
                    () -> assertEquals("131289",
                            estabelecimentoRepository.findById(estabelecimento.getId()).orElseThrow().getCodigoAcesso())
            );
        }

        @Test
        @DisplayName("Quando alteramos um estabelecimento com o codigo de acesso de outro estabelecimento")
        void quandoAtualizamosEstabelecimentoComCodigoDeAcessoErrado() throws Exception {
            // Arrange
            String codigoOriginal = estabelecimento.getCodigoAcesso();
            estabelecimentoPutRequestDTO.setCodigoAcesso("131289");

            // Act
            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", "999999")
                            .content(objectMapper.writeValueAsString(estabelecimentoPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage()),
                    () -> assertEquals(codigoOriginal,
                            estabelecimentoRepository.findById(estabelecimento.getId()).orElseThrow().getCodigoAcesso())
            );
        }

        @Test
        @DisplayName("Quando alteramos um estabelecimento com codigo de acesso inválido")
        void quandoAlterarEstabelecimentoInvalido() throws Exception {
            // Arrange
            EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("13")
                    .build();

            // Act
            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando criamos um novo estabelecimento com dados inválidos")
        void quandoCriarEstabelecimentoInvalido() throws Exception {
            // Arrange
            EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("13")
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_ESTABELECIMENTOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimentoPostPutRequestDTO.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento")
        void quandoBuscarCardapioEstabelecimento() throws Exception {
            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .build();

            Sabor sabor2 = Sabor.builder()
                    .nome("Mussarela")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("salgado")
                    .build();
            Sabor sabor3 = Sabor.builder()
                    .nome("Chocolate")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("doce")
                    .build();

            Sabor sabor4 = Sabor.builder()
                    .nome("Morango")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("doce")
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1, sabor2, sabor3, sabor4))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/sabores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<SaborResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(4, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento que não existe")
        void quandoBuscarCardapioEstabelecimentoInexistente() throws Exception {
            // Arrange
            // Nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + 9999 + "/sabores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento por tipo (salgado)")
        void quandoBuscarCardapioEstabelecimentoPorTipo() throws Exception {
            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .build();

            Sabor sabor2 = Sabor.builder()
                    .nome("Mussarela")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("salgado")
                    .build();
            Sabor sabor3 = Sabor.builder()
                    .nome("Chocolate")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("doce")
                    .build();

            Sabor sabor4 = Sabor.builder()
                    .nome("Morango")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("doce")
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1, sabor2, sabor3, sabor4))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", "salgado")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<SaborResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(2, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento por tipo (doce)")
        void quandoBuscarCardapioEstabelecimentoPorTipoDoce() throws Exception {
            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .build();

            Sabor sabor2 = Sabor.builder()
                    .nome("Mussarela")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("salgado")
                    .build();
            Sabor sabor3 = Sabor.builder()
                    .nome("Chocolate")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("doce")
                    .build();

            Sabor sabor4 = Sabor.builder()
                    .nome("Morango")
                    .precoM(20.0)
                    .precoG(30.0)
                    .tipo("doce")
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1, sabor2, sabor3, sabor4))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", "doce")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<SaborResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(2, resultado.size())
            );
        }
        @Test
        @DisplayName("Quando alteramos a disponibilidade do sabor no cardapio de um estabelecimento")
        void quandoAlterarDisponibilidadeSaborCardapio() throws Exception {
            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .build();

            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(patch(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/" + sabor1.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("disponibilidade", "false")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Boolean resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(false, resultado);

        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade do sabor no cardapio de um estabelecimento com o id de sabor inválido")
        void quandoAlterarDisponibilidadeComIdSaborInvalido() throws Exception {
            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .build();

            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(patch(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/" + 2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("disponibilidade", "false")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O sabor consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade do sabor no cardapio para indiponível deve ser indicado no cardápio")
        void quandoAlterarDisponibilidadeSaborCardapioVisualizacao() throws Exception {
            // Arrange
            Sabor sabor1 = Sabor.builder()
                    .nome("Calabresa")
                    .precoM(25.0)
                    .precoG(35.0)
                    .tipo("salgado")
                    .build();

            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .sabores(Set.of(sabor1))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(patch(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/" + sabor1.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("disponibilidade", "false")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Boolean resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertFalse(resultado);

        }

    }
}
