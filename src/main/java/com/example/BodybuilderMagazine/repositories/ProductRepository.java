package com.example.BodybuilderMagazine.repositories;

import com.example.BodybuilderMagazine.entity.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductsEntity, Integer> {

    List<ProductsEntity> findByName(String name);
}
