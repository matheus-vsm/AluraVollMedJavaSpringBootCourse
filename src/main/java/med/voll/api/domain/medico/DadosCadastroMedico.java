package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroMedico(
        @NotBlank // verifica se o campo esta nullo ou vazio
        String nome,
        @NotBlank // apenas para strings
        @Email // veficica se está no formato de email
        String email,
        @NotBlank
        String telefone,
        @NotBlank
        @Pattern(regexp = "\\d{4,6}") // expressão regular: campo representa um digito
                                      // numerico 0-9 e tem no min 4 e max 6 carac
        String crm,
        @NotNull
        Especialidade especialidade,
        @NotNull
        @Valid // ativa a validação dos atributos do objeto, verificando todas as anotações de
               // Bean Validation dentro dele.
        DadosEndereco endereco) {
}
