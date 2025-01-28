package rj.rest.apikey;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BasicAuthTest {

    @Test
    public void testBasicAuthentication() {
        // Set Base URI
        RestAssured.baseURI = "http://localhost:8080/rjrestauthapikey2";

        // Send GET request with Basic Authentication credentials
        given().log().all()
            .auth()
            .basic("user", "password") // Provide username and password
        .when()
            .get("/secure-data") // Secure endpoint
        .then().log().all()
            .assertThat()
            .statusCode(200) // Assert that the status code is 200 (OK)
            .body(equalTo("This is secured data!")); // Verify the response body
    }
    
    @Test
    public void testBasicAuthenticationNegative() {
        // Set Base URI
        RestAssured.baseURI = "http://localhost:8080/rjrestauthapikey2";

        // Send GET request with Basic Authentication credentials
        given().log().all()
         //   .auth()
          //  .basic("user", "password") // Provide username and password
        .when()
            .get("/secure-data") // Secure endpoint
        .then().log().all()
            .assertThat()
            .statusCode(401); // Assert that the status code is 200 (OK)
           // .body(equalTo("This is secured data!")); // Verify the response body
    }
}
