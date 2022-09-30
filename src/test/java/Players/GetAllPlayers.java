package Players;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import utilities.ReadProperties;

import java.util.Properties;

import static io.restassured.RestAssured.given;

public class GetAllPlayers {

    private static Properties config = new ReadProperties().loadProperties();

    private static final String baseUrl = config.getProperty("baseURL");

    private static final String getAllPlayersEndpoint = config.getProperty("allPlayersEndpoint");

    private JSONParser rawParser = new JSONParser();

    @BeforeEach
    public void before() {
        RestAssured.baseURI = baseUrl;
    }

    /**
     * Tests without parameters for endpoint: /players
     *
     * @throws ParseException
     */
    @Test
    public void getAllPlayersWithoutParams() throws ParseException {

        ValidatableResponse res = given().log().all().contentType(ContentType.JSON).get(getAllPlayersEndpoint).then()
                .statusCode(200);
        //Extract the string from the response and convert it to json to better access the data.
        JSONObject body = (JSONObject) rawParser.parse(res.extract().response().body().prettyPrint());
        JSONObject data = (JSONObject) ((JSONArray) body.get("data")).get(0);
        JSONObject meta = (JSONObject) body.get("meta");
        //Multiple data is checked, such as page number and data per page, as well as the first data on the page which
        // will be the values to be changed when testing with parameters.
        Assertions.assertAll(
                () -> Assertions.assertEquals(data.get("id").toString(), "14", "The fist_name is incorrect"),
                () -> Assertions.assertEquals(meta.get("per_page").toString(), "25", "Data per page doesn't match"),
                () -> Assertions.assertEquals(meta.get("current_page").toString(), "1", "Current page doesn't match"));

    }

    /**
     *
     * @param queryParam name of the query param
     * @param set value that is going to be set
     * @param outputData name of the response body key
     * @param expected value that is expected to be obtained
     * @throws ParseException
     */
    @ParameterizedTest(name = "{index} => queryParam={0}, set={1}, outputName={2}, expected{3}")
    @CsvSource({
            "page, 20, current_page, 20",
            "page, -12, current_page, 1",
            "page, !, current_page, 1",
            "per_page, 2, per_page, 2",
            "per_page, 20000, per_page, 100"


    })
    public void getAllPlayersWithParams(String queryParam, String set, String outputData,String expected) throws ParseException {

        ValidatableResponse res = given().log().all().contentType(ContentType.JSON).queryParam(queryParam, set).get(getAllPlayersEndpoint).then()
                .statusCode(200);

        JSONObject body = (JSONObject) rawParser.parse(res.extract().response().body().prettyPrint());
        JSONObject meta = (JSONObject) body.get("meta");

        Assertions.assertEquals(meta.get(outputData).toString(), expected, "Data per page doesn't match");

    }

    /**
     * Tests with search parameter for endpoint: /players
     *
     * @throws ParseException
     */
    @Test
    public void getAllPlayersWithSearch() throws ParseException {

        ValidatableResponse res = given().log().all().contentType(ContentType.JSON).queryParam("search", "james").get(getAllPlayersEndpoint).then()
                .statusCode(200);
        //Extract the string from the response and convert it to json to better access the data.
        JSONObject body = (JSONObject) rawParser.parse(res.extract().response().body().prettyPrint());
        JSONArray data = (JSONArray) body.get("data");
        //go through the json array to check that at least one of the two, first name or last name,
        // appears the search that has been specified, first setting it to lowercase.
        for (int i = 0; i < data.size(); i++) {
            if (((JSONObject) data.get(i)).get("last_name").toString().toLowerCase().contains("james") || ((JSONObject) data.get(i)).get("first_name").toString().toLowerCase().contains("james")) {
                Assertions.assertTrue(true);
            } else
                Assertions.fail("Name doen't appear");

        }


    }

}
