package ru.t1.restassured.tests.cart;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.t1.restassured.common.Constants;
import ru.t1.restassured.dto.CartItemDto;
import ru.t1.restassured.dto.Credentials;
import ru.t1.restassured.tests.BaseApiTest;
import ru.t1.restassured.util.RestUtil;

import static ru.t1.restassured.common.ResponseMessages.*;

/**
 * Тестовый класс для негативных тестов корзины (эндпоинт /cart, метод DELETE).
 * Для простоты и обеспечения независимости тестов в каждом тесте, где необходима аутентификация,
 * регистрируется и логинится новый пользователь.
 */
public class CartDeleteNegativeTest extends BaseApiTest {

    @Test
    @Tag("negative")
    public void deleteCartNegativeWithoutToken() {
        Response response = RestUtil.execDelete(Constants.CART_ENDPOINT + "/1");
        response.then().log().all().assertThat().statusCode(401);
        Assertions.assertEquals(MISSING_AUTHORIZATION_HEADER, response.jsonPath().getString("msg"));
    }

    @Test
    @Tag("negative")
    public void deleteCartNegativeInvalidToken() {
        String token = "invalid_token";
        Response response = RestUtil.execDelete(Constants.CART_ENDPOINT + "/1", token);
        response.then().log().all().assertThat().statusCode(422);
        Assertions.assertEquals(BAD_AUTHORIZATION_HEADER, response.jsonPath().getString("msg"));
    }

    @Test
    @Tag("negative")
    public void deleteCartNegativeNewUserWithoutCart() {
        Credentials credentials = registerUniqueUser();
        String token = authenticate(credentials);
        Response response = RestUtil.execGet(Constants.CART_ENDPOINT + "/1", token);
        response.then().log().all().assertThat().statusCode(404);
        Assertions.assertEquals(PRODUCT_NOT_FOUND, response.jsonPath().getString("msg"));
    }

    @Test
    @Tag("negative")
    public void deleteCartNegativeSeveralTimes() {
        Credentials credentials = registerUniqueUser();
        String token = authenticate(credentials);
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");
        CartItemDto cartItem = new CartItemDto().setProductId(id).setQuantity(1);
        RestUtil.execPost(Constants.CART_ENDPOINT, cartItem, token)
                .then().log().all().assertThat().statusCode(201);

        RestUtil.execDelete(Constants.CART_ENDPOINT + "/" + id, token)
                .then().log().all().assertThat().statusCode(200);
        Response response = RestUtil.execDelete(Constants.CART_ENDPOINT + "/" + id, token);
        response.then().log().all().assertThat().statusCode(404);
        Assertions.assertEquals(PRODUCT_NOT_FOUND_IN_CART, response.jsonPath().getString("message"));
    }

    @Test
    @Tag("negative")
    public void deleteCartNegativeUnlistedProductId() {
        Credentials credentials = registerUniqueUser();
        String token = authenticate(credentials);
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");
        CartItemDto cartItem = new CartItemDto().setProductId(id).setQuantity(1);
        RestUtil.execPost(Constants.CART_ENDPOINT, cartItem, token)
                .then().log().all().assertThat().statusCode(201);

        long invalid_id = id + 5;
        Response response = RestUtil.execDelete(Constants.CART_ENDPOINT + "/" + invalid_id, token);
        response.then().log().all().assertThat().statusCode(404);
        Assertions.assertEquals(PRODUCT_NOT_FOUND_IN_CART, response.jsonPath().getString("message"));
    }

}
