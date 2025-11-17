package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // indica que as configurações de segurança serão personalizadas
public class SecurityConfigurations {
    @Autowired
    private SecurityFilter securityFilter;

    @Bean // devolve um objeto para o Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) // desabilita a proteção contra ataques do tipo Cross-Site Request Forgery
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // configura a aplicação como STATELESS, não guarda estado. padrão API REST
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/login").permitAll(); // toda requisição POST /login que chegar será liberada
                    req.anyRequest().authenticated(); // qualquer outra requisição precisará ser autenticada
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // define que o meu filtro vai ser chamado primeiro
                .build();
    }

    @Bean // metodo para criar o objeto AuthenticationManager, para injetar a dependencia no controller
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean // ensina o spring que o BCrypt é o algoritmo de hash de senha que esta sendo armazenado no banco
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
