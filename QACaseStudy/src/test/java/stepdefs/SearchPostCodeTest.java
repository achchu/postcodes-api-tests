package stepdefs;

import com.jayway.restassured.response.Response;
import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import utils.PostcodeAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Class to test lookup scenarios
 */
public class SearchPostCodeTest {

    private int actualStatusCode;
    private List<Map<String, String>> resultValuesList;
    private String postcode;
    private Response bulkPostcodes;
    private List<String> multiplePostcodes;

    @Before
    public void setup() {

    }

    /**
     * Method to postcode for specific parameter
     *
     * @param postcode
     */
    @When("^user query for postcode with \"([^\\“]*)\"$")
    public void enterPostcode(String postcode) {
        this.postcode = postcode;
        Response response = given().log().all().param("q", this.postcode).when().get(PostcodeAPI.endPoint);
        this.resultValuesList = response.body().jsonPath().getList("result");
    }

    /**
     * Method to validate search result
     */
    @Then("user should receive list of postcodes starting with above strings")
    public void validatePostcode() {

        for (Map<String, String> info2 : this.resultValuesList) {
            boolean isActualCodeStartingWithQuery = false;

            String actualCode = info2.get("postcode");

            if (actualCode.startsWith(this.postcode)) {
                isActualCodeStartingWithQuery = true;
            }
            assertTrue(isActualCodeStartingWithQuery);
        }
    }

    /**
     * Method to bulk lookup postcode
     *
     * @param postcodes
     */
    @When("^user requests bulk lookup postcodes$")
    public void bulkLookUpPostcode(DataTable postcodes) {

        List<List<String>> postcodeInputsList = postcodes.raw();

        Map<String, List<String>> postcodesMap = new HashMap<String, List<String>>();
        this.multiplePostcodes = new ArrayList<>();

        for (List<String> postcodeList : postcodeInputsList) {
            String firstPostcode = postcodeList.get(0);
            this.multiplePostcodes.add(firstPostcode);
        }

        postcodesMap.put("postcodes", this.multiplePostcodes);

        Response response = given().log().all().contentType("application/json").body(postcodesMap).when().post(PostcodeAPI.endPoint);
        this.bulkPostcodes = response;

    }

    /**
     * Method to validate bulk lookup postcode request
     */
    @Then("the correct response should be given")
    public void validateBulkLookUp() {
        assertEquals(this.bulkPostcodes.statusCode(), 200);
        int actualCount = this.bulkPostcodes.body().jsonPath().getList("result").size();
        assertEquals(actualCount, this.multiplePostcodes.size());
    }

    /**
     * Method to postcode postcode with specific parameters
     *
     * @param postcode
     */
    @When("^user requests end point with \"([^\\“]*)\"$")
    public void requestPostcode(String postcode) {
        Response response = get(PostcodeAPI.endPoint + "/" + postcode);
        this.actualStatusCode = response.statusCode();
    }

    /**
     * Method to validate api responses for different parameters
     *
     * @param expectedStatusCode
     */
    @Then("^user should be able to see response with \"([^\\“]*)\"$")
    public void validateStatusCode(int expectedStatusCode) {
        assertEquals(this.actualStatusCode, expectedStatusCode);
    }
}


