package com.lazysyntax.nutron.nutrition.security.jwt;

import com.lazysyntax.nutron.nutrition.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String userId = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                userId = jwtService.extractUserId(jwt);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token ha expirado: {" + e.getMessage() + "}");
                handleJwtException(response, HttpStatus.UNAUTHORIZED, "JWT_EXPIRED", "El token JWT ha caducado. Por favor, solicite un nuevo token.");
                return;
            } catch (MalformedJwtException e) {
                logger.warn("JWT Token está mal formado: {" + e.getMessage() + "}");
                handleJwtException(response, HttpStatus.BAD_REQUEST, "MALFORMED_JWT", "El token JWT está mal formado.");
                return;
            } catch (Exception e) {
                logger.warn("JWT Token inválido o error desconocido: {" + e.getMessage() + "}");
                handleJwtException(response, HttpStatus.UNAUTHORIZED, "INVALID_JWT", "Token JWT inválido o error desconocido.");
                return;
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String or is missing.");
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.isTokenValid(jwt)) {
                UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(
                        userId, null, new ArrayList<>());

                userAuthentication
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(userAuthentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void handleJwtException(
            HttpServletResponse response,
            HttpStatus status,
            String errorCode,
            String errorMessage) throws IOException
    {

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (status == HttpStatus.UNAUTHORIZED) {
            /* Añade el encabezado 'WWW-Authenticate' para que cliente de Ktor en la app frontend pueda solicitar un
            * nuevo token.
            * */
            String wwwAuthenticateHeader = String.format("Bearer error=\"%s\", error_description=\"%s\"", errorCode, errorMessage);
            response.setHeader("WWW-Authenticate", wwwAuthenticateHeader);
        }

        String jsonError = String.format("{\n  \"errorCode\": \"%s\",\n  \"message\": \"%s\"\n}", errorCode, errorMessage);
        response.getWriter().write(jsonError);
    }
}
