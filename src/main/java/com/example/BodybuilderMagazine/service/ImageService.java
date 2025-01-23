package com.example.BodybuilderMagazine.service;

import com.example.BodybuilderMagazine.entity.Image;
import com.example.BodybuilderMagazine.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    public Image getImageById(Long id) {
        logger.info("Поиск изображения с ID: {}", id);
        return imageRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Изображение с ID {} не найдено", id);
                    return new RuntimeException("Изображение с ID " + id + " не найдено");
                });
    }
}
