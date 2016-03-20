import com.jayway.restassured.response.Response;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by vadim on 20.03.16.
 */
public class TestWrestler {
    static String URL = "http://streamtv.net.ua/base/php/";
    static String cookieName = "PHPSESSID";

    static int wrestlerID;

    // ===== create =====
    @Test(priority = 0)
    public void testCreateValidWrestler() {
        String input = "{\"lname\":\"Hughes\", \"fname\":\"John\", \"mname\":\"David\",\"dob\":\"01-01-1990\", \"style\":\"1\", \"region1\":\"11\", \"region2\":\"10\", \"fst1\":\"2\", \"fst2\":\"6\",\"expires\":\"2016\", \"lictype\":\"1\", \"card_state\":\"1\"}";

        Response createNewWrestlerResponse =

                given().
                        cookie(cookieName, getPHPSessionID()).
                        and().
                        body(input).
                        with().
                        contentType("application/json").
                        when().
                        post(URL + "wrestler/create.php").
                        then().
                        statusCode(200).
                        and().
                        extract().response();

        assertThat(createNewWrestlerResponse.getHeaders().hasHeaderWithName("content-type"), is(true));
        assertThat(createNewWrestlerResponse.getBody().jsonPath().get("result").toString(), is("true"));
        wrestlerID = createNewWrestlerResponse.getBody().jsonPath().get("id");
        assertThat(wrestlerID, is(notNullValue()));
    }

    // ===== read =====
    @Test(dependsOnMethods = {"testCreateValidWrestler"},priority = 1)
    public void testReadValidWrestler() {
        Response readWrestlerResponse =

                given().
                        cookie(cookieName, getPHPSessionID()).
                        when().
                        get(URL + "wrestler/read.php?id=" + wrestlerID).
                        then().
                        statusCode(200).
                        and().
                        extract().response();

        assertThat(readWrestlerResponse.getBody().jsonPath().get("id_wrestler"), is(notNullValue()));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("lname").toString(), is("Hughes"));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("fname").toString(), is("John"));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("mname").toString(), is("David"));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("dob").toString(), is("01-01-1990"));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("style").toString(), is("1"));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("region1").toString(), is("11"));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("region2").toString(), is("10"));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("fst1").toString(), is("2"));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("fst2").toString(), is("6"));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("trainerid1"), is(nullValue()));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("trainerid2"), is(nullValue()));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("trainer1"), is(nullValue()));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("trainer2"), is(nullValue()));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("expires").toString(), is("2016"));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("lictype").toString(), is("1"));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("card_state").toString(), is("1"));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("photo").toString(), is(""));
        assertThat(readWrestlerResponse.getBody().jsonPath().get("attaches").toString(), is("[]"));
    }

    @Test(priority = 1)
    public void testReadInvalidWrestler() {
        Response readWrestlerResponse =

                given().
                        cookie(cookieName, getPHPSessionID()).
                        when().
                        get(URL + "wrestler/read.php?id=-5").
                        then().
                        statusCode(200).
                        and().
                        extract().response();

        assertThat(readWrestlerResponse.getBody().jsonPath().get("id_wrestler"), is(nullValue()));
    }
    // ===== update =====
    @Test(dependsOnMethods = {"testCreateValidWrestler"},priority = 1)
    public void testUpdateValidWrestler() {
        String input = "{\"id_wrestler\":\"" + wrestlerID + "\",\"lname\":\"Hughes1\", \"fname\":\"John\", \"mname\":\"David\",\"dob\":\"01-01-1990\", \"style\":\"1\", \"region1\":\"11\", \"region2\":\"10\", \"fst1\":\"2\", \"fst2\":\"6\",\"expires\":\"2014\", \"lictype\":\"1\", \"card_state\":\"1\"}";

        Response updateWrestlerResponse =

                given().
                        cookie(cookieName, getPHPSessionID()).
                        and().
                        body(input).
                        with().
                        contentType("application/json").
                        when().
                        post(URL + "wrestler/update.php").
                        then().
                        statusCode(200).
                        and().
                        extract().response();

        assertThat(updateWrestlerResponse.getBody().jsonPath().get("result").toString(), is("true"));

    }
    // ===== fetch =====
    @Test(dependsOnMethods = {"testCreateValidWrestler"},priority = 1)
    public void testFetchValidWrestler() {

        Response fetchWrestlerResponse =

                given().
                        cookie(cookieName, getPHPSessionID()).
                        when().
                        get(URL + "wrestler/fetch.php?id=" + wrestlerID).
                        then().
                        statusCode(200).
                        and().
                        extract().response();

        assertThat(fetchWrestlerResponse.getBody().jsonPath().get("region2").toString(), is("10"));
        assertThat(fetchWrestlerResponse.getBody().jsonPath().get("trainerid1"), is(nullValue()));
        assertThat(fetchWrestlerResponse.getBody().jsonPath().get("trainerid2"), is(nullValue()));
        assertThat(fetchWrestlerResponse.getBody().jsonPath().get("trainer1"), is(nullValue()));
        assertThat(fetchWrestlerResponse.getBody().jsonPath().get("trainer2"), is(nullValue()));
        assertThat(fetchWrestlerResponse.getBody().jsonPath().get("card_state").toString(), is("1"));
        assertThat(fetchWrestlerResponse.getBody().jsonPath().get("attaches").toString(), is("[]"));
    }

    @Test(priority = 1)
    public void testFetchInvalidWrestler() {
        Response fetchWrestlerResponse =
        given().
                cookie(cookieName, getPHPSessionID()).
                when().
                get(URL + "wrestler/fetch.php?id=0").
                then().
                statusCode(200).
                and().
                extract().response();

        assertThat(fetchWrestlerResponse.getBody().jsonPath().get("id_wrestler"), is(nullValue()));
    }

    // ===== search =====
    @Test(dependsOnMethods = {"testCreateValidWrestler"},priority = 1)
    public void testSearchValidlWrestler() {
        String input = "{\"count\":\"25\", \"order\":\"lname+ASC\", \"search\":\"hughes\", \"start\":\"0\"}";

        Response searchWrestlerResponse =

                given().
                        cookie(cookieName, getPHPSessionID()).
                        and().
                        body(input).
                        with().
                        contentType("application/json").
                        when().
                        post(URL + "wrestler/search.php").
                        then().
                        statusCode(200).
                        and().
                        extract().response();

        assertFalse(searchWrestlerResponse.getBody().jsonPath().get("total").toString().equalsIgnoreCase("0"));
    }

    // ===== delete =====
    @Test(dependsOnMethods = {"testCreateValidWrestler"},priority = 2)
    public void testDeletelValidWrestler() {
        Response deleteWrestlerResponse =
                given().
                        cookie("PHPSESSID", getPHPSessionID()).
                        when().
                        get(URL + "wrestler/delete.php?id=" + wrestlerID).
                        then().
                        statusCode(200).
                        and().
                        extract().response();

        assertThat(deleteWrestlerResponse.getBody().jsonPath().get("result").toString(), is("true"));
    }

    @Test(priority = 2)
    public void testDeletelInvalidWrestler() {
        Response deleteWrestlerResponse =
        given().
                cookie(cookieName, getPHPSessionID()).
                when().
                get(URL + "wrestler/delete.php?id=0").
                then().
                statusCode(200).
                and().
                extract().response();
        assertThat(deleteWrestlerResponse.getBody().jsonPath().get("result").toString(), is("true"));
    }

    public static String getPHPSessionID() {

        Response response =
                given().
                        body("{ \"username\" : \"auto\", \"password\" : \"test\"}").
                        with().
                        contentType("application/json").
                        when().
                        post(URL + "login.php");

        return response.getCookie("PHPSESSID");
    }
}
