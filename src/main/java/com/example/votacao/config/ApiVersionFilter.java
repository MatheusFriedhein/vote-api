package com.example.votacao.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiVersionFilter extends OncePerRequestFilter {

    private static final String VERSION_HEADER = "X-API-Version";

    private final String expectedVersion;

    public ApiVersionFilter(@Value("${api.version:v1}") String expectedVersion) {
        this.expectedVersion = expectedVersion;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String headerVersion = request.getHeader(VERSION_HEADER);

        if (headerVersion != null && !headerVersion.isBlank() && !isAcceptedVersion(headerVersion)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Versão de API não suportada. Use " + expectedVersion + "\"}");
            return;
        }

        response.setHeader(VERSION_HEADER, expectedVersion);
        filterChain.doFilter(request, response);
    }

    private boolean isAcceptedVersion(String headerVersion) {
        return expectedVersion.equalsIgnoreCase(headerVersion)
                || expectedVersion.replace("v", "").equalsIgnoreCase(headerVersion);
    }
}
