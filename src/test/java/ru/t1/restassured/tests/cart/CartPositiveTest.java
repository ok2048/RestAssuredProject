package ru.t1.restassured.tests.cart;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.t1.restassured.common.Constants;
import ru.t1.restassured.dto.CartItemDto;
import ru.t1.restassured.dto.Credentials;
import ru.t1.restassured.tests.BaseApiTest;
import ru.t1.restassured.util.RestUtil;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

/**
 * Тестовый класс для позитивных тестов корзины (эндпоинт /cart, методы GET, POST, DELETE).
 * Для простоты и обеспечения независимости тестов в каждом тесте регистрируется и логинится новый пользователь.
 */
public class CartPositiveTest extends BaseApiTest {

    /**
     * Тест на получение списка товаров в корзине.
     * Шаги:
     * 1. Для создания корзины добавить в нее товар.
     * 2. Получить список товаров в корзине.
     * 3. Проверить соответствие ответа схеме
     */
    @Test
    public void getCart() {
        Credentials credentials = registerUniqueUser();
        String token = authenticate(credentials);
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");
        int quantity = 2;
        CartItemDto cartItem = new CartItemDto().setProductId(id).setQuantity(quantity);
        RestUtil.execPost(Constants.CART_ENDPOINT, cartItem, token)
                .then().log().all().assertThat().statusCode(201);

        RestUtil.execGet(Constants.CART_ENDPOINT, token)
                .then().log().all().assertThat().statusCode(200)
                .and().assertThat()
                .body(matchesJsonSchemaInClasspath("cart_item.json"));
    }

    /**
     * Тест на добавление товаров в корзину.
     * Шаги:
     * 1. Положить в корзину quantity товаров.
     * 2. Проверить, что в корзине quantity товаров.
     * 3. Положить в корзину quantity товаров.
     * 4. Проверить, что в корзине quantity*2 товаров.
     */
    @Test
    public void postCart() {
        Credentials credentials = registerUniqueUser();
        String token = authenticate(credentials);
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");
        int quantity = 2;
        CartItemDto cartItem = new CartItemDto().setProductId(id).setQuantity(quantity);
        RestUtil.execPost(Constants.CART_ENDPOINT, cartItem, token)
                .then().log().all().assertThat().statusCode(201);

        Response response = RestUtil.execGet(Constants.CART_ENDPOINT, token);
        response.then().log().all().assertThat().statusCode(200);
        Assertions.assertEquals(id, response.jsonPath().getLong("cart[0].id"));
        Assertions.assertEquals(quantity, response.jsonPath().getInt("cart[0].quantity"));

        RestUtil.execPost(Constants.CART_ENDPOINT, cartItem, token)
                .then().log().all().assertThat().statusCode(201);

        response = RestUtil.execGet(Constants.CART_ENDPOINT, token);
        response.then().log().all().assertThat().statusCode(200);
        Assertions.assertEquals(id, response.jsonPath().getLong("cart[0].id"));
        Assertions.assertEquals(quantity * 2, response.jsonPath().getInt("cart[0].quantity"));
    }

    /**
     * Тест на удаление из корзины.
     * Шаги:
     * 1. Положить в корзину 2 товара.
     * 2. Удалить товар из корзины.
     * 3. Проверить, что в корзине остался 1 товар.
     * 4. Удалить товар из корзины.
     * 5. Проверить, что корзина пуста
     */
    @Test
    public void deleteCart() {
        Credentials credentials = registerUniqueUser();
        String token = authenticate(credentials);
        long id = RestUtil.execGet(Constants.PRODUCTS_ENDPOINT)
                .then().extract().body().jsonPath().getInt("[0].id");
        CartItemDto cartItem = new CartItemDto().setProductId(id).setQuantity(2);
        RestUtil.execPost(Constants.CART_ENDPOINT, cartItem, token)
                .then().log().all().assertThat().statusCode(201);

        RestUtil.execDelete(Constants.CART_ENDPOINT + "/" + id, token)
                .then().log().all().assertThat().statusCode(200);
        Response response = RestUtil.execGet(Constants.CART_ENDPOINT, token);
        response.then().log().all().assertThat().statusCode(200);
        Assertions.assertEquals(id, response.jsonPath().getLong("cart[0].id"));
        Assertions.assertEquals(1, response.jsonPath().getInt("cart[0].quantity"));

        RestUtil.execDelete(Constants.CART_ENDPOINT + "/" + id, token)
                .then().log().all().assertThat().statusCode(200);
        RestUtil.execGet(Constants.CART_ENDPOINT, token)
                .then().log().all().assertThat().statusCode(200)
                .and().assertThat()
                .body("cart", hasSize(equalTo(0)));
    }

}
