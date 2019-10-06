package com.rest.test.test.step.definitions;
import static org.hamcrest.Matchers.equalTo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.StringUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

public class openWeather {
    private static String APIKey = "?APPID=2da8f1a243b6988efe0384664a48979b";
    private String ENDPOINT = "http://api.openweathermap.org/data/3.0/stations";
    private String DELETE_ENDPOINT = "http://api.openweathermap.org/data/3.0/stations/";
    private static Map<String, String> UNIQUEID_STATION_MAP = new HashMap<String, String>();
    private static List<String> EXTERNAL_ID_MAP = new ArrayList<String>();
    private static List<String> NAME_MAP = new ArrayList<String>();
    private static List<String> LATITUDE_MAP = new ArrayList<String>();
    private static List<String> LONGITUDE_MAP = new ArrayList<String>();
    private static List<String> ALTITUDE_MAP = new ArrayList<String>();
    private RequestSpecification request;
    private Response response;
    private ValidatableResponse json;
    private JsonPath jsonPath;
    private static int SUBMITTED_STATIONS_COUNT = 0;

    @Given("^I want to register station (.+) API key$")
    public void registerWithoutAPI(String preference) {
        if (preference.equals("with")) {
            ENDPOINT = ENDPOINT + APIKey;
        }
    }

    @When("^I provide the header with (.+) as (.+)$")
    public void provide_Header(String key, String value) {
        request = given().header(key, value);
    }

    @Then("^I submit the POST request with request body as$")
    public void postRequest(String message) {
        response = request.body(message).post(ENDPOINT);
    }

    @Then("^the response status code is (.+)$")
    public void verifyStatus(int code) {
        json = response.then().assertThat().statusCode(equalTo(code));
    }

    @Then("^response body includes the following$")
    public void validateResponseBody(Map<String, String> responseFields) {
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if (StringUtils.isNumeric(field.getValue())) {
                json.body(field.getKey(), equalTo(Integer.parseInt(field.getValue())));
            } else {
                json.body(field.getKey(), equalTo(field.getValue()));
            }
        }
    }

    @And("^I store the response fields$")
    public void storeID() {
        SUBMITTED_STATIONS_COUNT++;
        jsonPath = response.jsonPath();
        UNIQUEID_STATION_MAP.put(jsonPath.getString("external_id"), jsonPath.getString("ID"));
        EXTERNAL_ID_MAP.add(jsonPath.getString("external_id"));
        NAME_MAP.add(jsonPath.getString("name"));
        LATITUDE_MAP.add(jsonPath.getString("latitude"));
        LONGITUDE_MAP.add(jsonPath.getString("longitude"));
        ALTITUDE_MAP.add(jsonPath.getString("altitude"));
    }

    @When("^I submit GET request for stations$")
    public void submitGetRequest() {
        response = request.get(ENDPOINT+APIKey);
    }

    @When("^I see all the registered stations with its correct values$")
    public void validateStationValues() {
        jsonPath = response.jsonPath();
        for (int i=0; i<SUBMITTED_STATIONS_COUNT; i++){
            Assert.assertEquals("Value of external ID of station match", EXTERNAL_ID_MAP.get(i),jsonPath.getList("external_id").get(i));
            Assert.assertEquals("Value of name of station match", NAME_MAP.get(i),jsonPath.getList("name").get(i));
            Assert.assertEquals("Value of latitude of station match", LATITUDE_MAP.get(i),jsonPath.getList("latitude").get(i).toString());
            Assert.assertEquals("Value of longitude of station match", LONGITUDE_MAP.get(i),jsonPath.getList("longitude").get(i).toString());
            Assert.assertEquals("Value of altitude of station match", ALTITUDE_MAP.get(i),jsonPath.getList("altitude").get(i).toString());
        }
    }

    @When("^I submit the DELETE request with ID for the (.+)$")
    public void deleteStation(String externalID) {
        for (Map.Entry<String, String> entry : UNIQUEID_STATION_MAP.entrySet()) {
            if(entry.getKey().equals(externalID)){
            DELETE_ENDPOINT = ENDPOINT+"/"+entry.getValue()+APIKey;
                response = request.delete(DELETE_ENDPOINT);
            }
        }
    }
}
