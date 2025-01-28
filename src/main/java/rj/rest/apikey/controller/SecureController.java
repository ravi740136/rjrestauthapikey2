package rj.rest.apikey.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureController {

    @GetMapping("/secure-data")
    public String getSecureData() {
        // Since the API key is already validated by the filter, no further validation needed here.
        return "This is secured data!";
    }
}
