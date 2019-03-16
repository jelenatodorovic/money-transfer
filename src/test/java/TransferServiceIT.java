import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransferServiceIT {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void createAccounts() {
        Map<String, String> account = new HashMap<>();
        account.put("name", "Ana");
        account.put("balance", "1000");
        account.put("currency", "EUR");
        Response response = given().body(account).when().post("/accounts");
        Assert.assertEquals(201, response.getStatusCode());

        ResponseBody responseBody = response.getBody();
        JsonObject json = new JsonObject(responseBody.asString());

        Map<String, String> account1 = new HashMap<>();
        account1.put("name", "Bob");
        account1.put("balance", "800");
        account1.put("currency", "EUR");
        response = given().body(account1).when().post("/accounts");
        Assert.assertEquals(201, response.getStatusCode());

        responseBody = response.getBody();
        json = new JsonObject(responseBody.asString());
    }

    @Test
    public void createTransfer() {
        Map<String, String> transfer = new HashMap<>();
        transfer.put("fromId", 1 + "");
        transfer.put("toId", 2 + "");
        transfer.put("amount", "50.00");
        transfer.put("transferStatus", "CREATED");

        RequestSpecification request = given().body(transfer);
        Response response = request.post("/transfers");
        System.out.println(response.getBody().asString());
        Assert.assertEquals(201, response.getStatusCode());

        ResponseBody responseBody = response.getBody();
        JsonObject json = new JsonObject(responseBody.asString());
        String status = json.getString("transferStatus");
        Assert.assertEquals(status, "CREATED");

        Map<String, String> transferFinish = new HashMap<>();
        transferFinish.put("id", json.getInteger("id") + "");
        transferFinish.put("fromId", 1 + "");
        transferFinish.put("toId", 2 + "");
        transferFinish.put("amount", "50.00");
        transferFinish.put("transferStatus", "FINISHED");

        response = given().body(transferFinish).when().put("/transfers");
        Assert.assertEquals(200, response.getStatusCode());

        responseBody = response.getBody();
        json = new JsonObject(responseBody.asString());
        Assert.assertEquals(json.getString("transferStatus"), "FINISHED");
    }

    @Test
    public void testNotEnoughBalance() {
        Map<String, String> transfer = new HashMap<>();
        transfer.put("fromId", 1 + "");
        transfer.put("toId", 2 + "");
        transfer.put("amount", "5000.00");
        transfer.put("transferStatus", "CREATED");

        Response response = given().body(transfer).when().post("/transfers");
        Assert.assertEquals(500, response.getStatusCode());
    }

    @Test
    public void testNotFoundAccount() {
        Map<String, String> transfer = new HashMap<>();
        transfer.put("fromId", 100 + "");
        transfer.put("toId", 2 + "");
        transfer.put("amount", "50.00");
        transfer.put("transferStatus", "CREATED");

        Response response = given().body(transfer).when().post("/transfers");
        Assert.assertEquals(500, response.getStatusCode());

    }

    @AfterClass
    public static void tearDown() {
        RestAssured.reset();
    }
}
