package ru.t1.restassured.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import ru.t1.restassured.common.Constants;

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

}
