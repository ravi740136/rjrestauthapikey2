package rj.rest.apikey.fallback.config;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@Profile("key-basic")
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String VALID_API_KEY = "your-api-key"; // Ensure this matches the key in the request

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get the API key from the request header
        String apiKey = request.getHeader(API_KEY_HEADER);

        // Check if the API key is valid then no need of basic auth
        if (apiKey != null && apiKey.equals(VALID_API_KEY)) {
            // If API key is valid, create an authentication object
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    "apiUser", null, Collections.singletonList(new SimpleGrantedAuthority("USER"))
            );
        
            // Set the authentication context in the SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
            // Proceed with the filter chain (Basic Authentication after api key auth)
            filterChain.doFilter(request, response);
        }
    
}
