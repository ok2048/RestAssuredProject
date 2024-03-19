package ru.t1.restassured.util;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RestUtil {

    public static Response execGet(String path) {
        return RestAssured.given().log().all()
                .when().get(path);
    }

    public static Response execPost(String path, Object body) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post(path);
    }

    public static Response execPut(String path, Object body) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().put(path);
    }

    public static Response execDelete(String path) {
        return RestAssured.given().log().all()
                .when().delete(path);
    }
}
