package transfer;


import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import com.jayway.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;


public class TransferServiceIT {

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void getAllAccounts() {
       /* given()
                .when().get("accounts")
                .then().statusCode(200);*/
        get("/accounts").then()
                .assertThat()
                .statusCode(200);
    }
    @Test
    public void createAccounts() {
        System.out.println("test");
        RequestSpecification request = given();
        //request.header("Content-Type", "application/json");
        JsonObject object = new JsonObject();
        object.put("name", "John")
                .put("balance", 599)
                .put("currency", "EUR");
        /*request.body(
                "\t\"name\": \"Bob\",\n" +
                "\t\"balance\":\"500.00\",\n" +
                "\t\"currency\":\"EUR\"\n" +
                "}");*/
        request.body(object);
        Response response = request.post("/accounts");
        int statusCode = response.getStatusCode();
        ResponseBody responseBody = response.getBody();
        System.out.println(responseBody.prettyPrint());
        Assert.assertEquals(201, statusCode);
    }

    @After
    public void tearDown() {
        RestAssured.reset();
    }
}
