package ru.t1.restassured.tests.product;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.t1.restassured.common.Constants;
import ru.t1.restassured.tests.BaseApiTest;
import ru.t1.restassured.util.GenerationUtil;
import ru.t1.restassured.util.RestUtil;

public class ProductGetNegativeTest extends BaseApiTest {

    @Test
    @Tag("negative")
    public void getProductByUnlistedId() {
        long id = GenerationUtil.generateId();

        RestUtil.execGet(Constants.PRODUCTS_ENDPOINT + "/" + id)
                .then().log().all().assertThat().statusCode(404);
    }
}
