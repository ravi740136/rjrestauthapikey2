package rj.rest.apikey;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.PrintStream;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class FormLoginFallbackTest {
	
	@BeforeClass
	public void defaultSetup() {
		 RestAssured.baseURI = "http://localhost:8080/rjrestauthapikey2";
		  RestAssured.config = RestAssuredConfig.config()
	                .redirect(RedirectConfig.redirectConfig().followRedirects(false));
	      PrintStream logStream = System.out; // You can change this to log to a file

	        RestAssured.filters(
	                new RequestLoggingFilter(logStream),  // Logs requests
	                new ResponseLoggingFilter(logStream) // Logs responses
	        );
	
	}
	
	 @Test
	    public void testFallbackToFormLoginWhenApiKeyFails() {
	       
	        // Step 1: Attempt authentication with an invalid API key
	        given()
	            
	            .header("X-API-KEY", "invalid-api-key") // Invalid API key
	          //  .redirects().follow(false) // Prevent following redirects automatically
	        .when()
	            .get("/secure-data") // Try to access secure endpoint
	        .then()
	           
	            .assertThat()
	            .statusCode(302); // Expect redirection to form login

	        // Step 2: Perform Form Login manually
	        String sessionCookie = 
	            given()
	               
	                .contentType("application/x-www-form-urlencoded")
	                .formParam("username", "user")
	                .formParam("password", "password")
	             //   .redirects().follow(false) // Prevent automatic redirect after login
	            .when()
	                .post("/login") // Spring Security default login endpoint
	            .then()
	              
	                .assertThat()
	                .statusCode(302) // Expect redirection after login
	                .extract()
	                .cookie("JSESSIONID"); // Extract session ID for authentication

	        // Step 3: Use session cookie to access the secure resource
	        given()
	            
	            .cookie("JSESSIONID", sessionCookie) // Send session cookie after form login
	        .when()
	            .get("/secure-data")
	        .then()
	           
	            .assertThat()
	            .statusCode(200) // Expect success
	            .body(equalTo("This is secured data!"));
	    }
}
