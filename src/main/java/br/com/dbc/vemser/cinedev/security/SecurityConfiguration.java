package br.com.dbc.vemser.cinedev.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable().and()
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests((authz) ->
                                //autorizações -> auth
                                authz.antMatchers("/auth/fazer-login", "/auth/novo-cliente",
                                                "/auth/novo-cinema", "/auth/usuario-logado").permitAll()
                                        //autorizações -> cliente
                                        .antMatchers("/auth/atualizar-senha-cliente").hasRole("CLIENTE")
                                        .antMatchers("/auth/recuperar-senha-cliente").hasRole("CLIENTE")
                                        .antMatchers("/ingresso", "/ingressosComprados-cliente-logado",
                                                "/ingresso/comprar/{idCliente}/ingresso/{idIngresso}").hasRole("CLIENTE")
                                        .antMatchers(HttpMethod.GET, "/filme").hasRole("CLIENTE")
                                        .antMatchers("/cliente/atualizar-cliente-usuario").hasRole("CLIENTE")
                                        .antMatchers("/cliente/delete-cliente-logado").hasRole("CLIENTE")
                                        //autorizações - cinema
                                        .antMatchers("/auth/atualizar-senha-cinema").hasRole("CINEMA")
                                        .antMatchers("/auth/recuperar-senha-cinema").hasRole("CINEMA")
                                        .antMatchers(HttpMethod.GET, "/cinema/**").hasRole("CLIENTE")
                                        .antMatchers("/ingresso/**").hasRole("CINEMA")
                                        .antMatchers("/filme/**").hasRole("CINEMA")
                                        .antMatchers("/cinema/**").hasRole("CINEMA")
                                        //autorizações - administrador
                                        .antMatchers( "/avaliacoes/**").hasAnyRole("AVALIADOR","ADMIN")
                                        .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                                        .antMatchers("/auth/novo-admin").hasRole("ADMIN")
                                        .antMatchers("/**").hasRole("ADMIN")
                                        .antMatchers("/log/**").hasRole("ADMIN")
//
                                        .anyRequest().authenticated()
                );
        // 1 - verifica token
        http.addFilterBefore(new TokenAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .exposedHeaders("Authorization");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

