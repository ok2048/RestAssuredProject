package ru.t1.restassured.util;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RestUtil {

    public static Response execGet(String path) {
        return execGet(path, null);
    }

    public static Response execGet(String path, String token) {
        RequestSpecification specs = RestAssured.given().log().all();
        if (token != null) {
            specs.auth().oauth2(token);
        }
        return specs.when().get(path);
    }

    public static Response execPost(String path, Object body) {
        return execPost(path, body, null);
    }

    public static Response execPost(String path, Object body, String token) {
        RequestSpecification specs = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body);
        if (token != null) {
            specs.auth().oauth2(token);
        }
        return specs.when().post(path);
    }

    public static Response execPut(String path, Object body) {
        return execPut(path, body, null);
    }

    public static Response execPut(String path, Object body, String token) {
        RequestSpecification specs = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body);
        if (token != null) {
            specs.auth().oauth2(token);
        }
        return specs.when().put(path);
    }

    public static Response execDelete(String path) {
        return execDelete(path, null);
    }

    public static Response execDelete(String path, String token) {
        RequestSpecification specs = RestAssured.given().log().all();
        if (token != null) {
            specs.auth().oauth2(token);
        }
        return specs.when().delete(path);
    }
}
