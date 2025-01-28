package rj.rest.apikey.config;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rj.rest.apikey.model.ApiKey;
import rj.rest.apikey.repository.ApiKeyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Primary
@Profile("api-key")
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    private static final String API_KEY_HEADER = "x-api-key";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        String apiKey = request.getHeader(API_KEY_HEADER);

        if (apiKey != null) {
            // Retrieve API key from repository
            ApiKey key = apiKeyRepository.findByApiKey(apiKey);
            if (key != null) {
                // If valid API key, set authentication in context
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(key.getUsername(), null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // If invalid API key, return Unauthorized status
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Invalid API Key");
                return;
            }
        }
        // Proceed with the request filter chain
        filterChain.doFilter(request, response);
    }
}
