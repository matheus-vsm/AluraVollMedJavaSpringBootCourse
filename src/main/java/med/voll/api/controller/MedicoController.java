package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    @Autowired // "Crie e forneça automaticamente uma instância desse objeto aqui."
    private MedicoRepository repository;

    @PostMapping
    @Transactional // é uma anotação do Spring utilizada para controlar transações no banco de dados.
    //Ela garante que um conjunto de operações (inserir, atualizar, deletar, etc.)
    // seja executado como uma única transação, ou seja: Ou tudo acontece com sucesso, ou nada acontece.
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {
        repository.save(new Medico(dados));
    } // 201: Requisição Processada e Novo Recurso Criado

    @GetMapping
    // caso não seja passado na URL, por padrão, o tamanho das respostas é 10 e ordenado pelo nome
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        // buscar todos os dados de medico transforma cada elemento do stream | para cada Medico,
        // chama o construtor de Dados, convertendo
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);

        return ResponseEntity.ok(page); // 200
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
