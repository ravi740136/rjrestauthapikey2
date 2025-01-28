package rj.rest.apikey.basic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Profile("basic-auth")
@EnableWebSecurity
public class BasicSecurityConfig {

	  @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/h2-console/**").permitAll() // Allow H2 console without authentication
	                .anyRequest().authenticated() // Protect all other endpoints
	            )
	            .httpBasic(); // Enable Basic Authentication

	        return http.build();
	    }

	    @Bean
	    public UserDetailsService userDetailsService() {
	        UserDetails user = User.builder()
	            .username("user")
	            //encoder is used to encode the password
	            .password(passwordEncoder().encode("password")) // Encode password using BCrypt
	            .roles("USER")
	            .build();
	        return new InMemoryUserDetailsManager(user);
	    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for secure password encoding
    }
}
