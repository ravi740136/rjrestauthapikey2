package rj.rest.apikey.model;

import jakarta.persistence.Entity;  
import jakarta.persistence.GeneratedValue;  
import jakarta.persistence.GenerationType;  
import jakarta.persistence.Id;  

// Add these if necessary for lombok annotations
// import lombok.Data;  
// import lombok.NoArgsConstructor;  
// import lombok.AllArgsConstructor;  


@Entity
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	private String username;
    private String apiKey;

    // Constructors, getters, and setters
    public ApiKey() {}

    public ApiKey(String username, String apiKey) {
        this.username = username;
        this.apiKey = apiKey;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getters and Setters
}
