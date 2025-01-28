package rj.rest.apikey.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rj.rest.apikey.model.ApiKey;
 // Update the package name according to your project structure

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    ApiKey findByApiKey(String apiKey); // Finds an ApiKey entity by the API key
    int deleteByApiKey(String apiKey); // Deletes an ApiKey entity by the API key and returns the count of rows deleted
}

