package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.DadosListagemMedico;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {
        repository.save(new Medico(dados));
    }

    @GetMapping
    public List<DadosListagemMedico> listar() {
        return repository.findAll().stream()
                .map(DadosListagemMedico::new)
                .toList();
    }
}
