package rj.rest.apikey;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;

public class FormLoginTest {
	
	@BeforeClass
	public void defaultSetup() {
		 RestAssured.baseURI = "http://localhost:8080/rjrestauthapikey2";
		  RestAssured.config = RestAssuredConfig.config()
	                .redirect(RedirectConfig.redirectConfig().followRedirects(false));
	}
	
	 @Test
	    public void testFormLogin() {
	       
	      
	        // Step 2: Perform Form Login manually
	        String sessionCookie = 
	            given()
	                .log().all()
	                .contentType("application/x-www-form-urlencoded")
	                .formParam("username", "user")
	                .formParam("password", "password")
	             //   .redirects().follow(false) // Prevent automatic redirect after login
	            .when()
	                .post("/login") // Spring Security default login endpoint
	            .then()
	                .log().all()
	                .assertThat()
	                .statusCode(302) // Expect redirection after login
	                .extract()
	                .cookie("JSESSIONID"); // Extract session ID for authentication

	        // Step 3: Use session cookie to access the secure resource
	        given()
	            .log().all()
	            .cookie("JSESSIONID", sessionCookie) // Send session cookie after form login
	        .when()
	            .get("/secure-data")
	        .then()
	            .log().all()
	            .assertThat()
	            .statusCode(200) // Expect success
	            .body(equalTo("This is secured data!"));
	    }
}
