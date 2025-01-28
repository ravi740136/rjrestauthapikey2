package rj.rest.apikey;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiKeyAuthTest {

    private  String apiKey;

    
    @BeforeClass(dependsOnMethods = "requestDefaults")
    public  void setup() {
        
       // Obtain the API Key using Form Login before the test
      Response loginResponse   = given()
                    .formParam("username", "user") // Username parameter for form login
                    .formParam("password", "password") // Password parameter for form login
                    .when()
                        .post("/auth/login") // Endpoint that provides the API key
                    .then()
                        .statusCode(200) // Check that the status code is 200
                        .extract().response();
        
      apiKey = loginResponse.asString(); // Extract the API Key from the response
    
        String sessionId = loginResponse.cookie("JSESSIONID");

        // Set default session ID for all subsequent requests
        RestAssured.sessionId = sessionId;
    }
    
    @BeforeClass
   public void  requestDefaults(){
    	//apiKey = "91b09299-5ec9-4fab-a326-508466e93957";
    	// Set the base URI for your application
    	RestAssured.baseURI = "http://localhost:8080/rjrestauthapikey2";
    	
    	 // Configure RestAssured to use filters globally
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void testApiKeyAuthentication() {
    	
    	
        // Use the API key to authenticate and test access to a secured endpoint
        given()
            .header("x-api-key", apiKey) // Use the API key for authentication
        .when()
            .get("/secure-data") // Protected endpoint
        .then()
            .statusCode(200) // Expecting status 200 for successful access
            .body(equalTo("This is secured data!")); // Verify the response body
    }
    
    @Test
    public void testLogout() {
        // Log out and invalidate the API key
        given()
            .header("x-api-key", apiKey) // Provide API key for logout
        .when()
            .post("/auth/logout") // Logout endpoint
        .then()
            .statusCode(200) // Verify logout success
            .body(equalTo("Logged out successfully. API key invalidated."));
        given()
        .header("x-api-key", apiKey) // Use the API key for authentication
    .when()
        .get("/secure-data") // Protected endpoint
    .then()
        .statusCode(401); // Expecting status 401 for successful access
        
    
    }
    
/*    @AfterClass
    public void tearDown() {
        // Invalidate the API Key by logging out
        given()
            .header("x-api-key", apiKey) // Use the API key for logout
        .when()
            .delete("/auth/logout") // Endpoint for logout
        .then()
            .statusCode(200) // Ensure logout was successful
            .body(equalTo("Logged out successfully")); // Verify the response body

        // Optionally, clear any stored values
        apiKey = null;
    }*/
}
