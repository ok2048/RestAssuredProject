package ru.t1.restassured.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.instancio.Gen;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import ru.t1.restassured.common.Constants;
import ru.t1.restassured.dto.Credentials;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public abstract class BaseApiTest {

    protected static ObjectMapper objectMapper = new ObjectMapper();

    private static PrintStream logOutputStream;

    @BeforeAll
    public static void beforeAll() throws FileNotFoundException {
        logOutputStream = new PrintStream(new FileOutputStream("log.txt", true));

        RestAssured.filters(
                RequestLoggingFilter.logRequestTo(logOutputStream),
                ResponseLoggingFilter.logResponseTo(logOutputStream)
        );

        RestAssured.baseURI = Constants.BASE_URI;
    }

    @AfterAll
    public static void afterAll() {
        if (logOutputStream != null) {
            logOutputStream.close();
        }
    }


    protected static Credentials registerUniqueUser() {
        String username = Gen.string().length(6).get();
        String password = Gen.string().length(8).get();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"username\": \"" + username + "\"," +
                        "\"password\": \"" + password + "\"" +
                        "}")
                .post("/register");
        return new Credentials(username, password);
    }

    protected static String authenticate(Credentials credentials) {


        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when().post("/login")
                .then().extract().body().jsonPath().getString("access_token");
    }
}
