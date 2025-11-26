package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {
    @Autowired // "Crie e forneça automaticamente uma instância desse objeto aqui."
    private MedicoRepository repository;

    @PostMapping
    @Transactional // é uma anotação do Spring utilizada para controlar transações no banco de dados.
    //Ela garante que um conjunto de operações (inserir, atualizar, deletar, etc.)
    // seja executado como uma única transação, ou seja: Ou tudo acontece com sucesso, ou nada acontece.
    // Classe Spring que encapsula o endereço da API (http://localhost:8080/medicos...)
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
        var medico = new Medico(dados);
        repository.save(medico);
        // já cria o http://localhost:8080 e toUri cria o Objeto URI
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(ResponseEntity.ok(new DadosDetalhamentoMedico(medico)));
    } // 201: Requisição Processada e Novo Recurso Criado
    // Devolve os Dados do Novo Registro e um Cabeçalho do Protocolo HTTP (Location)

    @GetMapping
    // caso não seja passado na URL, por padrão, o tamanho das respostas é 10 e ordenado pelo nome
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        // buscar todos os dados de medico transforma cada elemento do stream | para cada Medico,
        // chama o construtor de Dados, convertendo
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);

        return ResponseEntity.ok(page); // 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoMedico> listarDetalhes(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
        // jpa rodando em uma transactional já identifica as mudancas e altera automaticamente no banco
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    // deleta o dado do banco
    @DeleteMapping("/deletar/{id}")
    @Transactional
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        medico.excluir();

        return ResponseEntity.noContent().build(); // 204: Requisição Processada e Sem Conteúdo
    }
}
