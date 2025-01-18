package com.example.BodybuilderMagazine.repository;

import com.example.BodybuilderMagazine.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
}
