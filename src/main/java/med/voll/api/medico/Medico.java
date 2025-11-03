package med.voll.api.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.endereco.Endereco;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter // gera o metodo get de todos os atributos
@NoArgsConstructor // gera o construtor padrao
@AllArgsConstructor // gera o construtor com todos os atributos
@EqualsAndHashCode(of = "id") // Essa anotação gera automaticamente os métodos equals() e hashCode()
// para a classe, usando somente o atributo "id" para comparação entre objetos.
public class Medico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String crm;
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;
    @Embedded // é usado para incluir um objeto dentro de uma entidade, sem criar uma tabela separada no banco
    // | a tabela Medicos conterá os atributos do endereço
    private Endereco endereco;

    public Medico(DadosCadastroMedico dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.crm = dados.crm();
        this.especialidade = dados.especialidade();
        this.endereco = new Endereco(dados.endereco());
    }
}
