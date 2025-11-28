package med.voll.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.ParameterObjectNamingStrategyCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest // Indica que é um teste de integração que carrega o contexto completo do Spring, para simular o controller
@AutoConfigureMockMvc // Configura o MockMvc para simular requisições HTTP
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc; // Simula requisições HTTP para o controller
    @Autowired
    private ParameterObjectNamingStrategyCustomizer parameterObjectNamingStrategyCustomizer;

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser // Simula um usuário autenticado para nao precisar lidar com segurança nesse teste
    void agendar_cenario1() throws Exception {
        var response = mvc.perform(post("/consultas")) // Simula uma requisição POST para o endpoint /agendar
                .andReturn().getResponse(); // Obtém a resposta da requisição

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value()); // Verifica se o status HTTP é 400 (Bad Request)
    }
}
