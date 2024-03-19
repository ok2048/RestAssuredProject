package ru.t1.restassured.util;

import lombok.experimental.UtilityClass;
import net.datafaker.Faker;
import org.instancio.Instancio;
import ru.t1.restassured.dto.ProductDto;

import static org.instancio.Select.field;

@UtilityClass
public class GenerationUtil {
    public static ProductDto generateProduct() {
        Faker faker = new Faker();
        return Instancio.of(ProductDto.class)
                .set(field(ProductDto::getName), faker.device().modelName())
                .set(field(ProductDto::getCategory), faker.industrySegments().sector())
                .set(field(ProductDto::getPrice), (float) faker.number().numberBetween(1, 1000))
                .set(field(ProductDto::getDiscount), faker.number().numberBetween(0, 99))
                .create();
    }

    public static long generateId() {
        Faker faker = new Faker();
        return faker.number().numberBetween(1000000,9999999);
    }
}
