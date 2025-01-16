package com.example.BodybuilderMagazine.models;

import org.springframework.web.multipart.MultipartFile;

public class ProductModel {
    private Long id;
    private String name;
    private String category;
    private String descriptions;
    private double price;
    private String brand;
    private MultipartFile image;

    public ProductModel() {
        this.id = id;
        this.name = name;
        this.category = category;
        this.descriptions = descriptions;
        this.price = price;
        this.brand = brand;
        this.image = image;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", descriptions='" + descriptions + '\'' +
                ", price=" + price +
                '}';
    }
}
