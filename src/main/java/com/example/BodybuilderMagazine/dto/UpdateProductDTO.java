package com.example.BodybuilderMagazine.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class UpdateProductDTO {

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

    @NotBlank(message = "Фото должно быть обязательно")
    private MultipartFile newImage;

    public UpdateProductDTO(Long id, String name, String category, String descriptions, double price, String brand, MultipartFile newImage) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.descriptions = descriptions;
        this.price = price;
        this.brand = brand;
        this.newImage = newImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Имя не должно быть пустым") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Имя не должно быть пустым") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Категория не должно быть пустой") String getCategory() {
        return category;
    }

    public void setCategory(@NotBlank(message = "Категория не должно быть пустой") String category) {
        this.category = category;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    @NotBlank(message = "Цена не должна быть нулевой")
    public double getPrice() {
        return price;
    }

    public void setPrice(@NotBlank(message = "Цена не должна быть нулевой") double price) {
        this.price = price;
    }

    public @NotBlank(message = "Бренд не должен быть пустым") String getBrand() {
        return brand;
    }

    public void setBrand(@NotBlank(message = "Бренд не должен быть пустым") String brand) {
        this.brand = brand;
    }

    @NotBlank(message = "Фото должно быть обязательно")
    public MultipartFile getNewImage() {
        return newImage;
    }

    public void setNewImage(@NotBlank(message = "Фото должно быть обязательно") MultipartFile image) {
        this.newImage = image;
    }
}
