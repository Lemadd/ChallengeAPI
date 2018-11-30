package hellofreshchallengeapi;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.hasItems;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;


public class CountryTest {

	String baseUrl = "http://services.groupkt.com/";
	String basePath = "/country";
	String[] validCountries = {"US", "DE", "GB"};
	String[] invalidCountries = {"TRE", "HY", "NB"};

    @Before
    public void setup (){
    	RestAssured.baseURI = baseUrl;
    	RestAssured.basePath = basePath;
    }

    @Test()
    public void getAllCountries(){
        Response response =  get("/get/all");
        response
                .then()
                .statusCode(200)
                .root("RestResponse.result.alpha2_code")
	            .log()
	            .body()
	            .body("", hasItems(validCountries));
    }
    @Test()
    public void getValidCountries(){
    	for (String country : validCountries) {
            Response response =  get("/get/iso2code/"+ country);
            response
	            .then()
	            .log()
	            .body()
	            .statusCode(200);
		}
    }
    @Test()
    public void getInvalidCountries(){
    	for (String country : invalidCountries) {
            Response response =  get("/get/iso2code/"+ country);
            response
		            .then()
		            .statusCode(200)
		            .root("RestResponse.messages")
		            .log()
		            .body()
		            .body("", Matchers.contains("No matching country found for requested code ["+ country +"]."));
		}
    }
    @Test()
    public void postCountry(){
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("Content-Type", "application/json");

        JsonObject country = new JsonObject();
        country.addProperty("name", "Test Country");
        country.addProperty("alpha2_code", "TC");
        country.addProperty("alpha3_code", "TCY");

        httpRequest.body(country.toString());
        
        Response response = httpRequest.post("/postMethodTest");
        response
		        .then()
	            .log()
	            .body()
		        .statusCode(200);
    }
}
