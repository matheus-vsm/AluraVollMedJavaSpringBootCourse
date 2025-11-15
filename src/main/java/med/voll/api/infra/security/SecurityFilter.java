package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // usado para que o spring carregue um classe/componente genérico
public class SecurityFilter extends OncePerRequestFilter { // classe para fazer a validação do token, é chamada
    // antes mesmo da requisição chegar no controller, intercepta
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // o envio do token é realizado em um cabeçalho/Header do protocolo HTTP, no caso o Authorization Header
        var tokenJWT = recuperarToken(request);
        System.out.println(tokenJWT);
        // manda seguir o fluxo de requisição / necessário para chmar os proximos filtros na aplicaçõ /
        // APENAS PARA SUCESSO, NAO RODAR ISSO QUANDO FOR PARA INTERROMPER A EXECUÇAÕ
        filterChain.doFilter(request, response); // filterChain = representa a cadeia de filtros da aplicação
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization"); // define qual o cabeçalho ele vai resgatar
        if (authorizationHeader == null) {
            throw new RuntimeException("Token JWT não enviado no cabeçalho Authorization!");
        }
        return authorizationHeader.replace("Bearer", ""); // tira o prefixo Bearer
    }
}
