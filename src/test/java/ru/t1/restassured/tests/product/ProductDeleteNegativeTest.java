package ru.t1.restassured.tests.product;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.t1.restassured.common.Constants;
import ru.t1.restassured.tests.BaseApiTest;
import ru.t1.restassured.util.GenerationUtil;
import ru.t1.restassured.util.RestUtil;

public class ProductDeleteNegativeTest extends BaseApiTest {
    @Test
    @Tag("negative")
    public void deleteProductByUnlistedId() {
        long id = GenerationUtil.generateId();

        RestUtil.execDelete(Constants.PRODUCTS_ENDPOINT + "/" + id)
                .then().log().all().assertThat().statusCode(404);
    }

    @Test
    @Tag("negative")
    public void deleteProductByIdSeveralTimes() {
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");

        RestUtil.execDelete(Constants.PRODUCTS_ENDPOINT + "/" + id)
                .then().log().all().assertThat().statusCode(200);
        RestUtil.execDelete(Constants.PRODUCTS_ENDPOINT + "/" + id)
                .then().log().all().assertThat().statusCode(404);
    }
}
