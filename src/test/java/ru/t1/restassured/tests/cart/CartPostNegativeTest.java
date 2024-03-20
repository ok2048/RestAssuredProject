package ru.t1.restassured.tests.cart;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.t1.restassured.common.Constants;
import ru.t1.restassured.dto.CartItemDto;
import ru.t1.restassured.dto.Credentials;
import ru.t1.restassured.tests.BaseApiTest;
import ru.t1.restassured.util.GenerationUtil;
import ru.t1.restassured.util.RestUtil;

import static ru.t1.restassured.common.ResponseMessages.*;

/**
 * Тестовый класс для негативных тестов корзины (эндпоинт /cart, метод POST).
 * Для простоты и обеспечения независимости тестов в каждом тесте, где необходима аутентификация,
 * регистрируется и логинится новый пользователь.
 */
public class CartPostNegativeTest extends BaseApiTest {

    @Test
    @Tag("negative")
    public void postCartNegativeWithoutToken() {
        long id = 1;
        int quantity = 2;
        CartItemDto cartItem = new CartItemDto().setProductId(id).setQuantity(quantity);
        Response response = RestUtil.execPost(Constants.CART_ENDPOINT, cartItem);
        response.then().log().all().assertThat().statusCode(401);
        Assertions.assertEquals(MISSING_AUTHORIZATION_HEADER, response.jsonPath().getString("msg"));
    }

    @Test
    @Tag("negative")
    public void postCartNegativeInvalidToken() {
        long id = 1;
        int quantity = 2;
        String token = "invalid token";
        CartItemDto cartItem = new CartItemDto().setProductId(id).setQuantity(quantity);
        Response response = RestUtil.execPost(Constants.CART_ENDPOINT, cartItem, token);
        response.then().log().all().assertThat().statusCode(422);
        Assertions.assertEquals(BAD_AUTHORIZATION_HEADER, response.jsonPath().getString("msg"));
    }

    @Test
    @Tag("negative")
    public void postCartNegativeEmptyProductId() {
        Credentials credentials = registerUniqueUser();
        String token = authenticate(credentials);
        int quantity = 2;
        CartItemDto cartItem = new CartItemDto().setQuantity(quantity);
        Response response = RestUtil.execPost(Constants.CART_ENDPOINT, cartItem, token);
        response.then().log().all().assertThat().statusCode(400);
        Assertions.assertEquals(BAD_REQUEST, response.jsonPath().getString("msg"));
    }

    @Test
    @Tag("negative")
    public void postCartNegativeEmptyQuantity() {
        Credentials credentials = registerUniqueUser();
        String token = authenticate(credentials);
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");
        CartItemDto cartItem = new CartItemDto().setProductId(id);
        Response response = RestUtil.execPost(Constants.CART_ENDPOINT, cartItem, token);
        response.then().log().all().assertThat().statusCode(400);
        Assertions.assertEquals(BAD_REQUEST, response.jsonPath().getString("msg"));
    }

    @Test
    @Tag("negative")
    public void postCartNegativeUnlistedProductId() {
        Credentials credentials = registerUniqueUser();
        String token = authenticate(credentials);
        long id = GenerationUtil.generateId();
        int quantity = 2;
        CartItemDto cartItem = new CartItemDto().setProductId(id).setQuantity(quantity);
        Response response = RestUtil.execPost(Constants.CART_ENDPOINT, cartItem, token);
        response.then().log().all().assertThat().statusCode(404);
        Assertions.assertEquals(PRODUCT_NOT_FOUND, response.jsonPath().getString("message"));
    }

    @Test
    @Tag("negative")
    public void postCartNegativeIncorrectQuantity() {
        Credentials credentials = registerUniqueUser();
        String token = authenticate(credentials);
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");
        int quantity = -1;
        CartItemDto cartItem = new CartItemDto().setProductId(id).setQuantity(quantity);
        Response response = RestUtil.execPost(Constants.CART_ENDPOINT, cartItem, token);
        response.then().log().all().assertThat().statusCode(400);
        Assertions.assertEquals(BAD_REQUEST, response.jsonPath().getString("message"));
    }
}