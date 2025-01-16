package com.example.BodybuilderMagazine.repositories;

import com.example.BodybuilderMagazine.entity.ProductsEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(ProductsEntity products) {
        String sql = "INSERT INTO products (name, category, descriptions, price, brand, image) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql, products.getName(), products.getCategory(), products.getDescriptions(),
                products.getPrice(), products.getBrand(), products.getImagePath());
    }

    public List<ProductsEntity> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProductsEntity.class));
    }

    public Optional<ProductsEntity> findById(int id) {
        String sql = "SELECT * FROM products where id=?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProductsEntity.class), id)
                .stream()
                .findFirst();
    }

    public int update(ProductsEntity products) {
        String sql = "UPDATE products SET name = ?, category = ?, descriptions = ?," +
                "price = ?, brand = ?, image = ?";
        return jdbcTemplate.update(sql, products.getName(), products.getCategory(),
                products.getDescriptions(), products.getPrice(), products.getBrand(),
                products.getName());
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<ProductsEntity> findByName(String name) {
        String sql = "SELECT * FROM products where name=?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProductsEntity.class));
    }
}














