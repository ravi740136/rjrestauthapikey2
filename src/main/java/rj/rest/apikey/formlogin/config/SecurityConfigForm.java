package rj.rest.apikey.formlogin.config;

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
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

@Profile("only-form")
@Configuration
public class SecurityConfigForm {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {       
	    http
	        .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
	        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // Allow frames for H2 Console
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/h2-console/**", "/login").permitAll() // Allow login and H2 Console
	            .anyRequest().authenticated()) // Protect all other endpoints
	        .formLogin(form -> form.loginPage("/login").permitAll()); // Enable Form Login    
	    return http.build();
	}


   /* @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {       
    	http
        .csrf(csrf -> csrf.disable())             //.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // Disable CSRF only for H2 Console
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // Allow frames for H2 Console
        .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/h2-console/**").permitAll() // Allow login and H2 console                 
                    .anyRequest().authenticated()) // Protect all other endpoints)        
        .formLogin(withDefaults()); // Enable Form Login as fallback // Redirect unauthenticated users;       
        return http.build();
    }*/

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("user")
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
