package com.lazysyntax.nutron.auth.security.jwt;

import com.lazysyntax.nutron.auth.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.jspecify.annotations.NonNull;
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
    private  JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String userId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                userId = jwtService.extractUserId(jwt);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token has expired: {" + e.getMessage() + "}");
                handleJwtException(response, HttpStatus.UNAUTHORIZED, "JWT_EXPIRED", "El token JWT ha caducado. Por favor, solicite un nuevo token.");
                return; // Stop further processing
            } catch (MalformedJwtException e) {
                logger.warn("JWT Token is malformed: {" + e.getMessage() + "}");
                handleJwtException(response, HttpStatus.BAD_REQUEST, "MALFORMED_JWT", "El token JWT está mal formado.");
                return; // Stop further processing
            } catch (Exception e) {
                logger.warn("Unable to parse JWT Token or other JWT error: {" + e.getMessage() + "}");
                handleJwtException(response, HttpStatus.UNAUTHORIZED, "INVALID_JWT", "Token JWT inválido o error desconocido.");
                return; // Stop further processing
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String or is missing.");
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if (jwtService.isTokenValid(jwt)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void handleJwtException(HttpServletResponse response, HttpStatus status, String errorCode, String errorMessage) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (status == HttpStatus.UNAUTHORIZED) {
            // Añadir el encabezado WWW-Authenticate para el cliente Ktor en la app frontend
            String wwwAuthenticateHeader = String.format("Bearer error=\"%s\", error_description=\"%s\"", errorCode, errorMessage);
            response.setHeader("WWW-Authenticate", wwwAuthenticateHeader);
        }

        String jsonError = String.format("{\n\t\"errorCode\": \"%s\",\n\t\"message\": \"%s\"\n}", errorCode, errorMessage);
        response.getWriter().write(jsonError);
    }
}