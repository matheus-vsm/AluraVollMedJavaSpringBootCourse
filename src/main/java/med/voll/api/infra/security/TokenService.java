package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {
    @Value("${api.security.token.secret}") // anotação para variavel ler o valor do application.properties
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Voll.med") // quem está gerando o token. geralmente é a identificação da aplication
                    .withSubject(usuario.getLogin()) // dono do token
                    .withClaim("Id", usuario.getId()) // metodo pra adicionar outras informações no token
                    .withExpiresAt(dataExpiracao()) // token expira daqui duas horas (definido no metodo)
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao Gerrar Token JWT", exception);
        }
    }

    // Instant é usado para representar um momento preciso, independentemente de fuso horário.
    // É o equivalente a um timestamp moderno.
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
