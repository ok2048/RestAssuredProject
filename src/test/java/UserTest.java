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

public class UserTest {
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
    public void registerTest() {
        String username = Gen.string().length(6).get();
        String password = Gen.string().length(8).get();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"username\": \"" + username + "\"," +
                        "\"password\": \"" + password + "\"" +
                        "}")
                .when().post("/register")
                .then().assertThat().statusCode(201);
    }

    @Test
    public void loginTest() {
        String username = Gen.string().length(6).get();
        String password = Gen.string().length(8).get();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"username\": \"" + username + "\"," +
                        "\"password\": \"" + password + "\"" +
                        "}")
                .when().post("/register")
                .then().assertThat().statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"username\": \"" + username + "\"," +
                        "\"password\": \"" + password + "\"" +
                        "}")
                .when().post("/login")
                .then().assertThat().statusCode(200);
    }

}
