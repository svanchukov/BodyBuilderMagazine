package com.example.BodybuilderMagazine.services;

import com.example.BodybuilderMagazine.dto.CreateNewProductDTO;
import com.example.BodybuilderMagazine.dto.UpdateProductDTO;
import com.example.BodybuilderMagazine.entity.ProductsEntity;
import com.example.BodybuilderMagazine.exceptions.ProductNotFoundException;
import com.example.BodybuilderMagazine.mappers.CreateNewProductMapper;
import com.example.BodybuilderMagazine.mappers.UpdateProductMapper;
import com.example.BodybuilderMagazine.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
public class ProductsService {

    private final ProductRepository productRepository;
    private final CreateNewProductMapper createNewProductMapper;
    private final UpdateProductMapper updateProductMapper;

    private static final Logger logger = LoggerFactory.getLogger(ProductsService.class);


    public ProductsService(ProductRepository productRepository, CreateNewProductMapper createNewProductMapper, UpdateProductMapper updateProductMapper) {
        this.productRepository = productRepository;
        this.createNewProductMapper = createNewProductMapper;
        this.updateProductMapper = updateProductMapper;
    }

    @Transactional
    public ProductsEntity saveProduct(CreateNewProductDTO createNewProductDTO) {
        logger.info("Создание нового продукта {}", createNewProductDTO.getName());
        ProductsEntity product = new ProductsEntity();
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


    @Transactional(readOnly = true)
    public String findAll(Model model) {
        List<ProductsEntity> allProducts = productRepository.findAll();
        model.addAttribute("products", allProducts);
        return "products";
    }

    @Transactional(readOnly = true)
    public String findById(Model model, @PathVariable("id") int id) {
        logger.info("Запрос на отображение продукта с ID: {}", id);
        Optional<ProductsEntity> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            return "productDetails";
        } else {
            logger.warn("Продукт с ID {} не найден", id);
            return "redirect:/error.html";
        }
    }

    @Transactional
    public void updateProduct(@PathVariable("id") int id, @ModelAttribute UpdateProductDTO updateProductDTO) {
        logger.info("Запрос на обновление продукта с ID: {}", id);
        ProductsEntity product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Продукт с ID {} не найден для обновления", id);
                    return new ProductNotFoundException("Продукт с ID " + id + " не найден");
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

        productRepository.update(product);
    }

    @Transactional(readOnly = true)
    public void showEditProductForm(int id, Model model) {
        logger.info("Запрос на редактирование продукта с ID: {}", id);
        Optional<ProductsEntity> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
        } else {
            logger.error("Продукт с ID {} не найден для редактирования", id);
            throw new ProductNotFoundException("Продукт с ID " + id + " не найден");
        }
    }


    @Transactional
    public void deleteProductById(@PathVariable("id") int id) {
        logger.info("Запрос на удаление продукта с ID: {}", id);
        ProductsEntity product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Продукт с ID {} не найден для удаления", id);
                    return new ProductNotFoundException("Продукт с ID " + id + " не найден");
                });

        productRepository.deleteById(id);
        logger.info("Продукт с ID {} успешно удален", id);
    }

    @Transactional(readOnly = true)
    public List<ProductsEntity> searchByName(String name) {
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






























