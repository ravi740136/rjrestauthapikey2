package rj.rest.apikey;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class FallbackAuthTest {

    @Test
    public void testWithCorrectAPIKeyWithoutBasic() {
        // Set Base URI
        RestAssured.baseURI = "http://localhost:8080/rjrestauthapikey2";

        // Attempt valid API Key authentication
        given().log().all()
        .header("X-API-KEY", "your-api-key")       
        .when()
            .get("/secure-data") // Secure endpoint
        .then().log().all()
            .assertThat()
            .statusCode(200) // Assert that the status code is 200 (OK)
            .body(equalTo("This is secured data!")); // Validate response body
    }
    
    @Test
    public void testFallbackToBasicAuthWhenApiKeyFails() {
        // Set Base URI
        RestAssured.baseURI = "http://localhost:8080/rjrestauthapikey2";

        // Attempt API Key authentication with an invalid key, then fallback to Basic Authentication
        given().log().all()
       
         .header("X-API-KEY", "invalid-api-key") // Provide an invalid API key
           .auth()
            .basic("user", "password") // Provide valid Basic Authentication credentials
        .when()
            .get("/secure-data") // Secure endpoint
        .then().log().all()
            .assertThat()
            .statusCode(200) // Assert that the status code is 200 (OK)
            .body(equalTo("This is secured data!")); // Validate response body
    }
    
    @Test
    public void testAuthenticationFailsIfInvalidAPIKeyWithoutBasic() {
        // Set Base URI
        RestAssured.baseURI = "http://localhost:8080/rjrestauthapikey2";

        // Attempt API Key authentication with an invalid key
        given().log().all()    
         .header("X-API-KEY", "invalid-api-key") // Provide an invalid API key           
        .when()
            .get("/secure-data") // Secure endpoint
        .then().log().all()
            .assertThat()
            .statusCode(401); // Assert that the status code is 401
           
    }
    
    @Test
    public void testAuthenticationFailsIfInvalidAPIKeyAndInvalidBasic() {
        // Set Base URI
        RestAssured.baseURI = "http://localhost:8080/rjrestauthapikey2";

        // Attempt API Key authentication with an invalid key, then fallback to Basic Authentication
        given().log().all()
       
         .header("X-API-KEY", "invalid-api-key") // Provide an invalid API key
           .auth()
            .basic("user", "ipassword") // Provide invalid Basic Authentication credentials
        .when()
            .get("/secure-data") // Secure endpoint
        .then().log().all()
            .assertThat()
            .statusCode(401); // Assert that the status code is 401
           
    }
}
