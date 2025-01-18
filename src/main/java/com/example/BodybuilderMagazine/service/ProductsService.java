package com.example.BodybuilderMagazine.service;

import com.example.BodybuilderMagazine.dto.CreateNewProductDTO;
import com.example.BodybuilderMagazine.dto.UpdateProductDTO;
import com.example.BodybuilderMagazine.entity.Photo;
import com.example.BodybuilderMagazine.entity.Product;
import com.example.BodybuilderMagazine.repository.PhotoRepository;
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
public class ProductsService {

    private final ProductRepository productRepository;
    private final PhotoRepository photoRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductsService.class);


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

                Photo photo = new Photo();
                photo.setImage(imageData);
                photo = photoRepository.save(photo);

                product.setPhoto(photo);
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

    public Optional<Product> findById(int id) {
        return productRepository.findById(id);
    }

    public void updateProduct(int id, UpdateProductDTO updateProductDTO) {
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

                Photo photo = product.getPhoto();
                if (photo == null) {
                    photo = new Photo();
                }

                photo.setImage(imageData);
                photo = photoRepository.save(photo);

                product.setPhoto(photo);
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






























