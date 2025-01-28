package rj.rest.apikey.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.context.annotation.Profile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Profile("api-key-session")
public class SessionAuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
 //       String username = credentials.get("username");
   //     String password = credentials.get("password");
System.out.println("==================="+username+"================"+password);
        if ("user".equals(username) && "password".equals(password)) {
        	 String apiKey = UUID.randomUUID().toString();
            // apiKeyRepository.save(new ApiKey(username, apiKey)); // Save to database for persistence

             // Store API key in session
            HttpSession session = request.getSession(true);
             session.setAttribute("apiKey", apiKey);
             session.setAttribute("username", username);
            return ResponseEntity.ok(apiKey);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
       HttpSession session = request.getSession(false);
        if (session != null) {
        session.invalidate(); // Invalidate the session, which removes the API key
       return ResponseEntity.ok("Logged out successfully. API key invalidated.");
        //return "Logged out successfully";
        }
        else {
            // No session exists; API key is invalid
        	return ResponseEntity.status(401).body("Unauthorized: No session available");
           
    }
}
}

