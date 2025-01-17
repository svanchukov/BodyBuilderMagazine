package com.example.BodybuilderMagazine.repositories;

import com.example.BodybuilderMagazine.entity.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Entity, Integer> {

    List<Entity> findByName(String name);

}














