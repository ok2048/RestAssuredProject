import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class ProductTest {

    private static PrintStream logOutputStream;

    @BeforeAll
    public static void beforeAll() throws FileNotFoundException {
        logOutputStream = new PrintStream(new FileOutputStream("log.txt", true));

        RestAssured.filters(
                RequestLoggingFilter.logRequestTo(logOutputStream),
                ResponseLoggingFilter.logResponseTo(logOutputStream)
        );

        RestAssured.baseURI = "http://localhost:5000";
    }

    @AfterAll
    public static void afterAll() {
        if (logOutputStream != null) {
            logOutputStream.close();
        }
    }

    @Test
    public void getAllProductsTest() {
        RestAssured.given().log().all()
                .when().get("/products")
                .then().assertThat().statusCode(200);
    }

    @Test
    public void postAddProductTest() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"name\": \"Xiaomi phone\"," +
                        "\"category\": \"Electronics\"," +
                        "\"price\": 159.99," +
                        "\"discount\": 5" +
                        "}")
                .when().post("/products")
                .then().assertThat().statusCode(201);
    }

    @Test
    public void getProductByIdTest() {
        long id = RestAssured.given().log().all()
                .when().get("/products")
                .then().extract().body().jsonPath().getInt("[0].id");

        RestAssured.given().log().all()
                .when().get("/products/" + id)
                .then().assertThat().statusCode(200);
    }

    @Test
    public void putUpdateProductByIdTest() {
        long id = RestAssured.given().log().all()
                .when().get("/products")
                .then().extract().body().jsonPath().getInt("[0].id");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"name\": \"Xiaomi phone\"," +
                        "\"category\": \"Electronics\"," +
                        "\"price\": 159.99," +
                        "\"discount\": 5" +
                        "}")
                .when().put("/products/" + id)
                .then().assertThat().statusCode(200);
    }

    @Test
    public void deleteProductByIdTest() {
        long id = RestAssured.given().log().all()
                .when().get("/products")
                .then().extract().body().jsonPath().getInt("[0].id");

        RestAssured.given().log().all()
                .when().delete("/products/" + id)
                .then().assertThat().statusCode(200);
    }

}
