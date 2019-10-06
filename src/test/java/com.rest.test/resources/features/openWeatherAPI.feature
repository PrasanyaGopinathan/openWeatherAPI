@openWeatherAPITests
Feature: Test the open weather station API

  Scenario: Register a weather station without API key
    Given I want to register station without API key
    And I provide the header with Content-Type as application/json
    When I submit the POST request with request body as
  """
  {
  "external_id": "DEMO_TEST002",
  "name": "Team Demo Test Station 002",
  "latitude": 33.33,
  "longitude": -122.43,
  "altitude": 222
  }
  """
    Then the response status code is 401
    And response body includes the following
      | cod     | 401                                                                               |
      | message | Invalid API key. Please see http://openweathermap.org/faq#error401 for more info. |
  @smoke
  Scenario Outline: Register a weather station with API key
    Given I want to register station with API key
    And I provide the header with Content-Type as application/json
    When I submit the POST request with request body as
    """
    <requestBody>
    """
    Then the response status code is 201
    And I store the response fields
  Examples:
    | requestBody |
    | {"external_id": "DEMO_TEST001","name": "Team Demo Test Station 001","latitude": 33.33,"longitude": -122.43,"altitude": 222}|
    | {"external_id": "DEMO_TEST002","name": "Team Demo Test Station 002","latitude": 33.33,"longitude": -122.43,"altitude": 222}|
  @smoke
  Scenario: Get all stations and validate the values stored
    Given I provide the header with Content-Type as application/json
    When I submit GET request for stations
    Then the response status code is 200
    And I see all the registered stations with its correct values
  @smoke
  Scenario Outline: Delete all registered stations
    Given I provide the header with Content-Type as application/json
    When I submit the DELETE request with ID for the <stations>
    Then the response status code is 204
    Examples:
      |stations|
      | DEMO_TEST001 |
      | DEMO_TEST002 |

  Scenario Outline: Validate all registered stations
    Given I provide the header with Content-Type as application/json
    When I submit the DELETE request with ID for the <stations>
    Then the response status code is 404
    And response body includes the following
      | code    | 404001 |
      | message | Station not found |
    Examples:
      |stations|
      | DEMO_TEST001 |
      | DEMO_TEST002 |
