package com.example.BodybuilderMagazine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products") // Приведено к нижнему регистру по конвенции
public class Product {

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

    @Setter
    @Column(name = "brand")
    private String brand;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id", referencedColumnName = "id")
    private Photo photo;

    public Product() {
    }

}
