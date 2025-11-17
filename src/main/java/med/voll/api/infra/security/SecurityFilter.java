package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // usado para que o spring carregue um classe/componente genérico
public class SecurityFilter extends OncePerRequestFilter { // classe para fazer a validação do token, é chamada
    // antes mesmo da requisição chegar no controller, intercepta

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // o envio do token é realizado em um cabeçalho/Header do protocolo HTTP, no caso o Authorization Header
        var tokenJWT = recuperarToken(request);
        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);
            var usuario = repository.findByLogin(subject);

            // config para que o spring entenda que o usuario está logado
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // manda seguir o fluxo de requisição / necessário para chmar os proximos filtros na aplicaçõ /
        // APENAS PARA SUCESSO, NAO RODAR ISSO QUANDO FOR PARA INTERROMPER A EXECUÇAÕ
        filterChain.doFilter(request, response); // filterChain = representa a cadeia de filtros da aplicação
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization"); // define qual o cabeçalho ele vai resgatar
        if (authorizationHeader == null) {
            return null;
        }
        return authorizationHeader.replace("Bearer ", "").trim(); // tira o prefixo Bearer
    }
}
