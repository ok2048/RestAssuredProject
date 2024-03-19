package ru.t1.restassured.tests.product;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.t1.restassured.common.Constants;
import ru.t1.restassured.tests.BaseApiTest;
import ru.t1.restassured.dto.ProductDto;
import ru.t1.restassured.util.GenerationUtil;
import ru.t1.restassured.util.RestUtil;

public class ProductPutNegativeTest extends BaseApiTest {
    @Test
    @Tag("negative")
    public void putUpdateProductByUnlistedId() {
        long id = GenerationUtil.generateId();

        ProductDto product = GenerationUtil.generateProduct();
        RestUtil.execPut(Constants.PRODUCTS_ENDPOINT + "/" + id, product)
                .then().log().all().assertThat().statusCode(404);
    }

    @Test
    @Tag("negative")
    public void putUpdateProductEmptyBody() {
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");

        RestUtil.execPut(Constants.PRODUCTS_ENDPOINT + "/" + id, "")
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void putUpdateProductNullName() {
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");

        ProductDto product = GenerationUtil.generateProduct();
        product.setName(null);
        RestUtil.execPut(Constants.PRODUCTS_ENDPOINT + "/" + id, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void putUpdateProductNullCategory() {
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");

        ProductDto product = GenerationUtil.generateProduct();
        product.setCategory(null);
        RestUtil.execPut(Constants.PRODUCTS_ENDPOINT + "/" + id, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void putUpdateProductNullPrice() {
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");

        ProductDto product = GenerationUtil.generateProduct();
        product.setPrice(null);
        RestUtil.execPut(Constants.PRODUCTS_ENDPOINT + "/" + id, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void putUpdateProductNullDiscount() {
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");

        ProductDto product = GenerationUtil.generateProduct();
        product.setDiscount(null);
        RestUtil.execPut(Constants.PRODUCTS_ENDPOINT + "/" + id, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void putUpdateProductIncorrectName() {
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");

        ProductDto product = GenerationUtil.generateProduct();
        product.setName("");
        RestUtil.execPut(Constants.PRODUCTS_ENDPOINT + "/" + id, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void putUpdateProductIncorrectCategory() {
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");

        ProductDto product = GenerationUtil.generateProduct();
        product.setCategory("");
        RestUtil.execPut(Constants.PRODUCTS_ENDPOINT + "/" + id, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void putUpdateProductIncorrectPrice() {
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");

        ProductDto product = GenerationUtil.generateProduct();
        product.setPrice(-1.00f);
        RestUtil.execPut(Constants.PRODUCTS_ENDPOINT + "/" + id, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Tag("negative")
    @ParameterizedTest(name = "{index} - {0} is incorrect discount")
    @ValueSource(ints = {-1, 101})
    public void putUpdateProductIncorrectDiscount(int discount) {
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");

        ProductDto product = GenerationUtil.generateProduct();
        product.setDiscount(discount);
        RestUtil.execPut(Constants.PRODUCTS_ENDPOINT + "/" + id, product)
                .then().log().all().assertThat().statusCode(400);
    }
}
