package com.example.BodybuilderMagazine.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public abstract class ProductDTO {

    private Long id;

    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    @NotBlank(message = "Категория не должно быть пустой")
    private String category;
    private String descriptions;

    @NotBlank(message = "Цена не должна быть нулевой")
    private double price;

    @NotBlank(message = "Бренд не должен быть пустым")
    private String brand;
}
