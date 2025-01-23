package com.example.BodybuilderMagazine.repository;

import com.example.BodybuilderMagazine.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
