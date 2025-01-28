package rj.rest.apikey.config;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import rj.rest.apikey.model.ApiKey;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Primary
@Profile("api-key-session")
public class ApiKeyFilter extends OncePerRequestFilter {
	 private static final String API_KEY_HEADER = "x-api-key";
	    private static final String SESSION_API_KEY_ATTRIBUTE = "apiKey";



	    @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	            throws ServletException, IOException {

	        HttpSession session = request.getSession(false); // Get the current session (if it exists)
	        String apiKey = request.getHeader(API_KEY_HEADER);

	        if (apiKey != null) {
	            if (session != null) {
	                // Check if the API key matches the session attribute
	                String sessionApiKey = (String) session.getAttribute(SESSION_API_KEY_ATTRIBUTE);
	                if (apiKey.equals(sessionApiKey)) {
	                    // Valid API key; set authentication in context
	                    UsernamePasswordAuthenticationToken authentication =
	                            new UsernamePasswordAuthenticationToken(session.getAttribute("username"), null, null);
	                    SecurityContextHolder.getContext().setAuthentication(authentication);
	                } else {
	                    // Invalid API key for session
	                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                    response.getWriter().write("Unauthorized: Invalid API Key for this session");
	                    return;
	                }
	            } else {
	                // No session exists; API key is invalid
	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                response.getWriter().write("Unauthorized: No session available");
	                return;
	            }
	        }

	        // Proceed with the filter chain if no API key or a valid session API key
	        filterChain.doFilter(request, response);
	    }
}
