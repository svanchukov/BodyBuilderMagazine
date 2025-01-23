package com.example.BodybuilderMagazine.service;

import com.example.BodybuilderMagazine.dto.CreateNewProductDTO;
import com.example.BodybuilderMagazine.dto.UpdateProductDTO;
import com.example.BodybuilderMagazine.entity.Image;
import com.example.BodybuilderMagazine.entity.Product;
import com.example.BodybuilderMagazine.repository.ImageRepository;
import com.example.BodybuilderMagazine.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageRepository photoRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);


    public Product saveProduct(CreateNewProductDTO createNewProductDTO) {
        logger.info("Создание нового продукта {}", createNewProductDTO.getName());
        Product product = new Product();
        product.setName(createNewProductDTO.getName());
        product.setCategory(createNewProductDTO.getCategory());
        product.setDescriptions(createNewProductDTO.getDescriptions());
        product.setPrice(createNewProductDTO.getPrice());
        product.setBrand(createNewProductDTO.getBrand());

        MultipartFile imageFile = createNewProductDTO.getImage();

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                byte[] imageData = imageFile.getBytes();

                Image image = new Image();
                image.setImage(imageData);
                image = photoRepository.save(image);

                product.setImage(image);
                logger.info("Сохраняем фото с данными: {}", Arrays.toString(imageData));
            } catch (IOException e) {
                logger.error("Ошибка при обработке изображения для продукта: {}", createNewProductDTO.getName(), e);
                throw new RuntimeException("Error while processing image", e);
            }
        }

        // Сохраняем продукт в базе данных
        productRepository.save(product);
        return product;
    }


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public void updateProduct(Long id, UpdateProductDTO updateProductDTO) {
        logger.info("Запрос на обновление продукта с ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Продукт с ID {} не найден для обновления", id);
                    return new RuntimeException("Продукт с ID " + id + " не найден");
                });


        // Обновляем основные данные продукта
        product.setName(updateProductDTO.getName());
        product.setCategory(updateProductDTO.getCategory());
        product.setBrand(updateProductDTO.getBrand());
        product.setPrice(updateProductDTO.getPrice());

        // Обновляем изображение, если новое изображение было загружено
        MultipartFile newImage = updateProductDTO.getNewImage();
        if (newImage != null && !newImage.isEmpty()) {
            try {
                byte[] imageData = newImage.getBytes();

                Image image = product.getImage();
                if (image == null) {
                    image = new Image();
                }

                image.setImage(imageData);
                image = photoRepository.save(image);

                product.setImage(image);
            } catch (IOException e) {
                logger.error("Ошибка при обновлении изображения для продукта с ID: {}", id, e);
                throw new RuntimeException("Ошибка при обновлении изображения", e);
            }
        }

        productRepository.save(product);
    }

    public void showEditProductForm(Long id, Model model) {
        logger.info("Запрос на редактирование продукта с ID: {}", id);
        productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Продукт с ID {} не найден для редактирования", id);
                    return new RuntimeException("Продукт с ID " + id + " не найден");
                });
    }


    public void delete(Long ProductId) {
        logger.info("Запрос на удаление продукта с ID: {}", ProductId);
        Product product = productRepository.findById(ProductId)
                .orElseThrow(() -> {
                    logger.error("Продукт с ID {} не найден для удаления", ProductId);
                    return new RuntimeException("Продукт с ID " + ProductId + " не найден");
                });

        productRepository.deleteById(ProductId);
        logger.info("Продукт с ID {} успешно удален", ProductId);
    }

    public List<Product> searchByName(String name) {
        logger.info("Запрос на поиск продукта по имени: {}", name);
        if (name != null && !name.isEmpty()) {
            return productRepository.findByName(name);
        }
        return productRepository.findAll();
    }


}






























