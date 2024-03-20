package ru.t1.restassured.tests.cart;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.t1.restassured.common.Constants;
import ru.t1.restassured.dto.Credentials;
import ru.t1.restassured.tests.BaseApiTest;
import ru.t1.restassured.util.RestUtil;

/**
 * Тестовый класс для негативных тестов корзины (эндпоинт /cart, метод GET).
 * Для простоты и обеспечения независимости тестов в каждом тесте, где необходима аутентификация,
 * регистрируется и логинится новый пользователь.
 */
public class CartGetNegativeTest extends BaseApiTest {

    @Test
    @Tag("negative")
    public void getCartNegativeWithoutToken() {
        RestUtil.execGet(Constants.CART_ENDPOINT)
                .then().log().all().assertThat().statusCode(401);
    }

    @Test
    @Tag("negative")
    public void getCartNegativeInvalidToken() {
        String token = "invalid_token";
        RestUtil.execGet(Constants.CART_ENDPOINT, token)
                .then().log().all().assertThat().statusCode(422);
    }

    @Test
    @Tag("negative")
    public void getCartNegativeNewUserWithoutCart() {
        Credentials credentials = registerUniqueUser();
        String token = authenticate(credentials);
        RestUtil.execGet(Constants.CART_ENDPOINT, token)
                .then().log().all().assertThat().statusCode(404);
    }
}
