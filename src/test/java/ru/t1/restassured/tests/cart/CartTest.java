package ru.t1.restassured.tests.cart;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.instancio.Gen;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class CartTest {
    private static PrintStream logOutputStream;
    private static String token;

    @BeforeAll
    public static void beforeAll() throws FileNotFoundException {
        logOutputStream = new PrintStream(new FileOutputStream("log.txt", true));

        RestAssured.filters(
                RequestLoggingFilter.logRequestTo(logOutputStream),
                ResponseLoggingFilter.logResponseTo(logOutputStream)
        );

        RestAssured.baseURI = "http://localhost:5000";

        String username = Gen.string().length(6).get();
        String password = Gen.string().length(8).get();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"username\": \"" + username + "\"," +
                        "\"password\": \"" + password + "\"" +
                        "}")
                .post("/register");

        token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"username\": \"" + username + "\"," +
                        "\"password\": \"" + password + "\"" +
                        "}")
                .when().post("/login")
                .then().extract().body().jsonPath().getString("access_token");
    }

    @AfterAll
    public static void afterAll() {
        if (logOutputStream != null) {
            logOutputStream.close();
        }
    }

    @Test
    public void getCartTest() {
        // Добавим в корзину товар, чтобы она не была пустой
        long id = RestAssured.given().log().all()
                .when().get("/products")
                .then().assertThat().statusCode(200)
                .and().extract().body().jsonPath().getInt("[0].id");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body("{" +
                        "\"product_id\":" + id + "," +
                        "\"quantity\": 2" +
                        "}")
                .when().post("/cart")
                .then().assertThat().statusCode(201);

        RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().get("/cart")
                .then().assertThat().statusCode(200);
    }

    @Test
    public void postCartTest() {
        long id = RestAssured.given().log().all()
                .when().get("/products")
                .then().assertThat().statusCode(200)
                .and().extract().body().jsonPath().getInt("[0].id");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body("{" +
                        "\"product_id\":" + id + "," +
                        "\"quantity\": 2" +
                        "}")
                .when().post("/cart")
                .then().assertThat().statusCode(201);
    }

    @Test
    public void deleteCartTest() {
        // Добавим в корзину товар, чтобы она не была пустой
        long id = RestAssured.given().log().all()
                .when().get("/products")
                .then().assertThat().statusCode(200)
                .and().extract().body().jsonPath().getInt("[0].id");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body("{" +
                        "\"product_id\":" + id + "," +
                        "\"quantity\": 2" +
                        "}")
                .when().post("/cart")
                .then().assertThat().statusCode(201);

        RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().delete("/cart/" + id)
                .then().assertThat().statusCode(200);
    }

}
