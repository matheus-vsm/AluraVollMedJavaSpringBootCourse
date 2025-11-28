package med.voll.api.controller;

import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.support.WebContentGenerator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
// Indica que é um teste de integração que carrega o contexto completo do Spring, para simular o controller
@AutoConfigureMockMvc // Configura o MockMvc para simular requisições HTTP
@AutoConfigureJsonTesters // Configura os testadores JSON para serialização e desserialização de objetos Java
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc; // Simula requisições HTTP para o controller
    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson; // Utilizado para serializar objetos Java em JSON
    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;
    @MockitoBean // Cria um mock do bean AgendaDeConsultas para injetá-lo no contexto do Spring durante o teste
    // nao injeta um AgendamentoDeConsultas real, mas sim um mock
    private AgendaDeConsultas agendaDeConsultas;
    @Autowired
    private WebContentGenerator webContentGenerator;

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
        // Simula um usuário autenticado para nao precisar lidar com segurança nesse teste
    void agendar_cenario1() throws Exception {
        var response = mvc.perform(post("/consultas")) // Simula uma requisição POST para o endpoint /agendar
                .andReturn().getResponse(); // Obtém a resposta da requisição

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value()); // Verifica se o status HTTP é 400 (Bad Request)    }
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informacoes estao validas")
    @WithMockUser
        // Simula um usuário autenticado para nao precisar lidar com segurança nesse teste
    void agendar_cenario2() throws Exception {
        var data = LocalDateTime.now().plusHours(1); // Data atual + 1 hor para ser no futuro
        var especialidade = Especialidade.CARDIOLOGIA;

        var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 2L, 5L, data);

        when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento); // Configura o mock para retornar os dados de detalhamento quando o método agendar for chamado

        var response = mvc
                .perform(
                        post("/consultas")
                                .contentType(MediaType.APPLICATION_JSON) // Define o tipo de conteúdo da requisição como JSON
                                .content(dadosAgendamentoConsultaJson.write(
                                                new DadosAgendamentoConsulta(2L, 5L, data, especialidade)
                                        ).getJson())).andReturn().getResponse(); // Obtém a resposta da requisição

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = dadosDetalhamentoConsultaJson.write(dadosDetalhamento).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado); // Verifica se o conteúdo da resposta é igual ao esperado}
    }
}