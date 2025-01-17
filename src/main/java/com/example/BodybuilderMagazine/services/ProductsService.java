package com.example.BodybuilderMagazine.services;

import com.example.BodybuilderMagazine.dto.CreateNewProductDTO;
import com.example.BodybuilderMagazine.dto.UpdateProductDTO;
import com.example.BodybuilderMagazine.entity.Entity;
import com.example.BodybuilderMagazine.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductsService.class);


    public Entity saveProduct(CreateNewProductDTO createNewProductDTO) {
        logger.info("Создание нового продукта {}", createNewProductDTO.getName());
        Entity product = new Entity();
        product.setName(createNewProductDTO.getName());
        product.setCategory(createNewProductDTO.getCategory());
        product.setDescriptions(createNewProductDTO.getDescriptions());
        product.setPrice(createNewProductDTO.getPrice());
        product.setBrand(createNewProductDTO.getBrand());

        MultipartFile imageFile = createNewProductDTO.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Путь к директории загрузки
                String uploadDir = "C:\\Users\\Пользователь\\Desktop\\BodybuilderMagazine\\src\\main\\resources\\static\\uploads\\images";
                Path uploadPath = Paths.get(uploadDir);

                // Создаем директорию, если она не существует
                if (Files.notExists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    logger.info("Создаём директорию если её не существует {}", uploadDir);
                }

                Path imagePath = uploadPath.resolve(imageFile.getOriginalFilename());
                Files.copy(imageFile.getInputStream(), imagePath);
                logger.info("Изображение успешно загружено: {}", imagePath.toString());

                // Устанавливаем путь к изображению в продукт
                product.setImagePath(imagePath.toString());
            } catch (IOException e) {
                logger.error("Ошибка при сохранении изображения для продукта: {}", createNewProductDTO.getName(), e);
                throw new RuntimeException("Error while saving image", e);
            }
        }

        // Сохраняем продукт в базе данных
        productRepository.save(product);
        return product;
    }


    public List<Entity> findAll() {
        return productRepository.findAll();
    }

    public Optional<Entity> findById(int id) {
        return productRepository.findById(id);
    }

    public void updateProduct(int id, UpdateProductDTO updateProductDTO) {
        logger.info("Запрос на обновление продукта с ID: {}", id);
        Entity product = productRepository.findById(id)
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
                // Удаляем старое изображение, если оно существует
                if (product.getImagePath() != null) {
                    Path oldImagePath = Paths.get(product.getImagePath());
                    Files.deleteIfExists(oldImagePath);
                    logger.info("Старое изображение удалено: {}", oldImagePath);
                }

                // Сохраняем новое изображение
                String newImagePath = saveImage(newImage);
                product.setImagePath(newImagePath);
                logger.info("Новое изображение успешно сохранено для продукта с ID: {}", id);
            } catch (IOException e) {
                logger.error("Ошибка при обновлении изображения для продукта с ID: {}", id, e);
                throw new RuntimeException("Ошибка при обновлении изображения", e);
            }
        }

        productRepository.save(product);
    }

    public void showEditProductForm(int id, Model model) {
        logger.info("Запрос на редактирование продукта с ID: {}", id);
        productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Продукт с ID {} не найден для редактирования", id);
                    return new RuntimeException("Продукт с ID " + id + " не найден");
                });
    }


    public void delete(int ProductId) {
        logger.info("Запрос на удаление продукта с ID: {}", ProductId);
        Entity product = productRepository.findById(ProductId)
                .orElseThrow(() -> {
                    logger.error("Продукт с ID {} не найден для удаления", ProductId);
                    return new RuntimeException("Продукт с ID " + ProductId + " не найден");
                });

        productRepository.deleteById(ProductId);
        logger.info("Продукт с ID {} успешно удален", ProductId);
    }

    public List<Entity> searchByName(String name) {
        logger.info("Запрос на поиск продукта по имени: {}", name);
        if (name != null && !name.isEmpty()) {
            return productRepository.findByName(name);
        }
        return productRepository.findAll();
    }

    private String saveImage(MultipartFile image) throws IOException {
        logger.info("Сохраняем изображение с именем: {}", image.getOriginalFilename());
        String uploadDir = "C:\\Users\\Пользователь\\Desktop\\BodybuilderMagazine\\src\\main\\resources\\static\\uploads\\images";
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.createDirectories(filePath.getParent()); // Создаем папку, если её нет
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        logger.info("Изображение успешно сохранено: {}", filePath.toString());
        return fileName; // Возвращаем только имя файла
    }


}






























