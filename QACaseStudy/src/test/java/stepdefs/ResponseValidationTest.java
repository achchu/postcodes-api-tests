package stepdefs;

import com.google.gson.Gson;
import com.jayway.restassured.response.Response;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javafx.geometry.Pos;
import org.testng.annotations.Test;
import utils.DbModule;
import utils.PostcodeAPI;

import java.util.Map;

import static com.jayway.restassured.RestAssured.get;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;


/**
 * Class to test api response and format
 */
public class ResponseValidationTest {

    private Response response;
    private Map<String, String> responseJson;
    private String formattedJson;
    private Map<String, String> resultValueList;
    private String actualRandomPostcode;


    @Before
    public void setup() {

    }

    /**
     * Method to query from the api
     */
    @When("a user queries for postcode")
    public void queryPostCode() {
        this.response = get(PostcodeAPI.endPoint + "/" + "NR330AU");
        this.responseJson = this.response.body().jsonPath().get();

        Gson gson = new Gson();
        this.formattedJson = gson.toJson(this.responseJson);
    }

    /**
     * Method to validate JSON format
     */
    @Then("it should produce expected JSON format")
    public void expectedResponse() {
        assertThat(this.formattedJson, matchesJsonSchemaInClasspath("responseSchema.json"));
    }

    /**
     * Method to query specific postcode
     *
     * @param specificPostcode
     */
    @When("^user queries for random \"([^\\“]*)\"$")
    public void randomPostCode(String specificPostcode) {
        Response response = get(PostcodeAPI.endPoint + "/" + specificPostcode);
        this.resultValueList = response.body().jsonPath().getMap("result");
        this.actualRandomPostcode = this.resultValueList.get("postcode");
    }

    /**
     * Method to validate specific postcode information with database
     */
    @Then("ensure that it matches the database")
    public void matchDB() {
        assertEquals(this.actualRandomPostcode, DbModule.getPostcodeFromDatabase());
    }
}
