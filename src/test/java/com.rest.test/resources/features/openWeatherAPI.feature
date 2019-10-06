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

  Scenario Outline: Register a weather station with API key
    Given I want to register station with API key
    And I provide the header with Content-Type as application/json
    When I submit the POST request with request body as
    """
    <requestBody>
    """
    Then the response status code is 201
   # And I store the unique ID returned in the response
  Examples:
    | requestBody |
    | {"external_id": "DEMO_TEST001","name": "Team Demo Test Station 001","latitude": 33.33,"longitude": -122.43,"altitude": 222}|
    | {"external_id": "DEMO_TEST002","name": "Team Demo Test Station 002","latitude": 33.33,"longitude": -122.43,"altitude": 222}|
  @open
  Scenario: test scenario
    Given I retrieve data from testdata1
#    Then add something

