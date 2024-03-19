package ru.t1.restassured.tests.product;

import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.Test;
import ru.t1.restassured.common.Constants;
import ru.t1.restassured.tests.BaseApiTest;
import ru.t1.restassured.dto.ProductDto;
import ru.t1.restassured.util.GenerationUtil;
import ru.t1.restassured.util.RestUtil;

public class ProductPositiveTest extends BaseApiTest {

    @Test
    public void getAllProducts() {
        RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().log().all().assertThat().statusCode(200)
                .and().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("product.json"));
    }

    @Test
    public void postAddNewProduct() {
        ProductDto product = GenerationUtil.generateProduct();
        RestUtil.execPost(Constants.PRODUCT_ENDPOINT, product)
                .then().log().all().assertThat().statusCode(201);
    }

    @Test
    public void getProductById() {
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");

        RestUtil.execGet(Constants.PRODUCTS_ENDPOINT + "/" + id)
                .then().log().all().assertThat().statusCode(200)
                .and().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("product.json"));
    }

    @Test
    public void putUpdateProductById() {
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");

        ProductDto product = GenerationUtil.generateProduct();
        RestUtil.execPut(Constants.PRODUCTS_ENDPOINT + "/" + id, product)
                .then().log().all().assertThat().statusCode(200);
    }

    @Test
    public void deleteProductById() {
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");

        RestUtil.execDelete(Constants.PRODUCTS_ENDPOINT + "/" + id)
                .then().log().all().assertThat().statusCode(200);
    }
}
