package com.rest.test.test.step.definitions;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.lang3.StringUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import com.rest.test.resources.Utilities.excelHelper;

public class openWeather {

    private String APIKey = "?APPID=2da8f1a243b6988efe0384664a48979b";
    private String POST_ENDPOINT = "http://api.openweathermap.org/data/3.0/stations";
    private RequestSpecification request;
    private Response response;
    private ValidatableResponse json;
    private Map<String, String>registerStationTestData;
    public excelHelper ex;
//

    @Given("^I want to register station (.+) API key$")
    public void registerWithoutAPI(String preference){
        if (preference.equals("with")){
            POST_ENDPOINT = POST_ENDPOINT+APIKey;
        }
    }

    @When("^I provide the header with (.+) as (.+)$")
    public void provide_Header(String key, String value){
        request = given().header(key, value);
    }

    @Then("^I submit the POST request with request body as$")
    public void postRequest(String message){
    response = request.body(message).post(POST_ENDPOINT);
    }
    @Then("^the response status code is (.+)$")
    public void verifyStatus(int code){

        json = response.then().assertThat().statusCode(equalTo(code));
      //  System.out.println("json"+json);

    }
    @Then("^response body includes the following$")
    public void validateResponseBody(Map<String,String> responseFields){
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if(StringUtils.isNumeric(field.getValue())) {
                json.body(field.getKey(), equalTo(Integer.parseInt(field.getValue())));
            }
            else{
                json.body(field.getKey(), equalTo(field.getValue()));
            }
        }
    }
   @Given("^I retrieve data from testdata1$")
    public void saveUniqueID() throws IOException{
       System.out.println("going inside");
       String fileLocation;
       System.out.println("@@@@@@@@@@@@@");
       List<Map<String,String>> data = new ArrayList<Map<String, String>>();
       FileOutputStream outputStream = new FileOutputStream("/Users/prasanyagopinathan/Documents/Dev/openWeatherStation/src/test/java/com.rest.test/resources/testdata/postRequest.xlsx");
       XSSFWorkbook workbook = new XSSFWorkbook();
               workbook.write(outputStream);
             int  index = workbook.getActiveSheetIndex();
       System.out.println("#########@"+index);
       XSSFSheet sheet = workbook.getSheetAt(index);
       outputStream.close();
       int rows = sheet.getPhysicalNumberOfRows();
       System.out.println("Rows"+rows);

       //int colummns = sheet.getColumns();
       for (int i=1; i <= rows; i++){

           Map<String,String> fields = new HashMap<String, String>();
           for (int j = 1; j < 6; j++) {

               fields.put(sheet.getCellComment(i,j).toString(), sheet.getCellComment(i,1).toString());

           }
           data.add(fields);}
       outputStream.close();
   }
//    add something

}
