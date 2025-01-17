package com.example.BodybuilderMagazine.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@jakarta.persistence.Entity
@Table(name = "Products")
public class Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "descriptions")
    private String descriptions;

    @Column(name = "price")
    private double price;

    @Column(name = "brand")
    private String brand;

    @Column(name = "image")
    private String imagePath;

}
