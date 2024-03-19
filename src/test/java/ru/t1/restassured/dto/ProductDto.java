package ru.t1.restassured.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductDto {
    private String name;
    private String category;
    private Float price;
    private Integer discount;
}
