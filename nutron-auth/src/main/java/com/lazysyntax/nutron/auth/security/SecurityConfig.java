package com.lazysyntax.nutron.auth.security;

import com.lazysyntax.nutron.auth.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Iterfaz PasswordEnoder (SpringSecurity), definde la forma de codificar y verificar contraeñas.
     * BCrupPaswordEncoder es implemntacion de esta interfaz. Usa el algoritmo de encriptacion "bcryppt",
     *  ressitente a atques de fuerza bruta y de diccionario.
     * 
     */

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Configuracion de la seguridad HTTP para las diferentes rutas
     **/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize //Configura las reglas de autorización
                        .requestMatchers("/api/v1/auth/**").permitAll() //Permite el acceso a estas rutas sin autenticación
                        .anyRequest().authenticated() // Cualquier otra solicitud requiere autenticacion por ejemplo: /api/v1/user/setup/**
                )
                /* Define la gestion de las sesiones.
                 * NO se crean sesiones para almacenar el estado del usuario. Cada vez que el usuario necesita un recurso se
                 * valida y autoriza al usuario.
                 * */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

                /*
                * Filtro personalizado para validar los tokens del usuario. Se comprueba la integridad de cada token  y su
                * tiempo de expiracion. En el caso de que haya una incidencia, se formula una cabecera especifica para
                * que pueda ser leida por la aplicacion cliente.
                * */
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
