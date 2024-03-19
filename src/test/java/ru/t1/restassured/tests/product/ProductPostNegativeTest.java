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

public class ProductPostNegativeTest extends BaseApiTest {
    @Test
    @Tag("negative")
    public void postProductEmptyBody() {
        RestUtil.execPost(Constants.PRODUCT_ENDPOINT, "")
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void postProductNullName() {
        ProductDto product = GenerationUtil.generateProduct();
        product.setName(null);
        RestUtil.execPost(Constants.PRODUCT_ENDPOINT, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void postProductNullCategory() {
        ProductDto product = GenerationUtil.generateProduct();
        product.setCategory(null);
        RestUtil.execPost(Constants.PRODUCT_ENDPOINT, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void postProductNullPrice() {
        ProductDto product = GenerationUtil.generateProduct();
        product.setPrice(null);
        RestUtil.execPost(Constants.PRODUCT_ENDPOINT, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void postProductNullDiscount() {
        ProductDto product = GenerationUtil.generateProduct();
        product.setDiscount(null);
        RestUtil.execPost(Constants.PRODUCT_ENDPOINT, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void postProductIncorrectName() {
        ProductDto product = GenerationUtil.generateProduct();
        product.setName("");
        RestUtil.execPost(Constants.PRODUCT_ENDPOINT, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void postProductIncorrectCategory() {
        ProductDto product = GenerationUtil.generateProduct();
        product.setCategory("");
        RestUtil.execPost(Constants.PRODUCT_ENDPOINT, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void postProductIncorrectPrice() {
        ProductDto product = GenerationUtil.generateProduct();
        product.setPrice(-1.00f);
        RestUtil.execPost(Constants.PRODUCT_ENDPOINT, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Tag("negative")
    @ParameterizedTest(name = "{index} - {0} is incorrect discount")
    @ValueSource(ints = {-1, 101})
    public void postProductIncorrectDiscount(int discount) {
        ProductDto product = GenerationUtil.generateProduct();
        product.setDiscount(discount);
        RestUtil.execPost(Constants.PRODUCT_ENDPOINT, product)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    @Tag("negative")
    public void postProductDuplicate() {
        ProductDto product = GenerationUtil.generateProduct();
        RestUtil.execPost(Constants.PRODUCT_ENDPOINT, product)
                .then().log().all().assertThat().statusCode(200);

        RestUtil.execPost(Constants.PRODUCT_ENDPOINT, product)
                .then().log().all().assertThat().statusCode(400);
    }
}
