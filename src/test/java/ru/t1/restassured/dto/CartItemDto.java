package ru.t1.restassured.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CartItemDto {
    @JsonProperty("product_id")
    private Long productId;
    private Integer quantity;
}
