package rj.rest.apikey.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import rj.rest.apikey.model.ApiKey;
import rj.rest.apikey.repository.ApiKeyRepository;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Profile("api-key")
public class AuthController {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        // Validate credentials (hardcoded for simplicity)
        if ("user".equals(username) && "password".equals(password)) {
            // Generate and store API key
            String apiKey = UUID.randomUUID().toString();
            ApiKey key = new ApiKey(username, apiKey);
            apiKeyRepository.save(key);
            return ResponseEntity.ok(apiKey);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("x-api-key") String apiKey) {
        // Check if the API key exists
        ApiKey key = apiKeyRepository.findByApiKey(apiKey);
        if (key != null) {
            apiKeyRepository.delete(key); // Delete the API key from the database
            return ResponseEntity.ok("Logged out successfully. API key invalidated.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API key not found.");
    }
}

