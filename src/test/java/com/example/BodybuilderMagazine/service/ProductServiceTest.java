package com.example.BodybuilderMagazine.service;

import com.example.BodybuilderMagazine.dto.CreateNewProductDTO;
import com.example.BodybuilderMagazine.dto.UpdateProductDTO;
import com.example.BodybuilderMagazine.entity.ProductsEntity;
import com.example.BodybuilderMagazine.exceptions.ProductNotFoundException;
import com.example.BodybuilderMagazine.repositories.ProductRepository;
import com.example.BodybuilderMagazine.services.ProductsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductsService productsService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MultipartFile mockImage;

    private CreateNewProductDTO createNewProductDTO;

    private List<ProductsEntity> mockProducts;

    @BeforeEach
    void setUp() throws IOException {
        mockImage = mock(MultipartFile.class);
        InputStream mockInputStream = getClass().getResourceAsStream("/testImage.jpg");
        lenient().when(mockImage.getInputStream()).thenReturn(mockInputStream);
        lenient().when(mockImage.getOriginalFilename()).thenReturn("testImage.jpg");
    }


    @Test
    void saveProduct() throws IOException {
        createNewProductDTO = new CreateNewProductDTO();
        createNewProductDTO.setName("Gainer");
        createNewProductDTO.setCategory("Gainers");
        createNewProductDTO.setDescriptions("Super Gainer");
        createNewProductDTO.setPrice(15.99);
        createNewProductDTO.setBrand("5LB");
        createNewProductDTO.setImage(mockImage);

        String uploadDir = "src/main/resources/static/uploads/images";
        Path uploadPath = Paths.get(uploadDir);
        if (Files.notExists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        System.out.println("Upload directory: " + uploadPath.toAbsolutePath());

        productsService.saveProduct(createNewProductDTO);

        verify(productRepository, times(1)).save(any(ProductsEntity.class));

        Path imagePath = uploadPath.resolve("testImage.jpg");
        assertTrue(Files.exists(imagePath));
    }


    @Test
    void findAll() {

        ProductsEntity product1 = new ProductsEntity();
        product1.setName("Protein");

        ProductsEntity product2 = new ProductsEntity();
        product2.setName("L-Carnitin");

        List<ProductsEntity> productsEntityList = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(productsEntityList);

        Model model = mock(Model.class);

        String viewName = productsService.findAll(model);

        verify(productRepository, times(1)).findAll();

        verify(model).addAttribute("products", productsEntityList);

        assertEquals("products", viewName);
    }

    @Test
    void findById() {
        ProductsEntity product1 = new ProductsEntity();
        product1.setId(1);

        when(productRepository.findById(1)).thenReturn(Optional.of(product1));

        Optional<ProductsEntity> result = productRepository.findById(1);

        Assertions.assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void updateProduct() {

        CreateNewProductDTO createNewProductDTO = new CreateNewProductDTO();
        createNewProductDTO.setId(1L);
        createNewProductDTO.setName("Gainer");
        createNewProductDTO.setCategory("Gainers");
        createNewProductDTO.setDescriptions("Super Gainer");
        createNewProductDTO.setPrice(15.99);
        createNewProductDTO.setBrand("5LB");
        createNewProductDTO.setImage(null);

        ProductsEntity savedProduct = productsService.saveProduct(createNewProductDTO);

        // Создаем DTO для обновления
        UpdateProductDTO updateProductDTO = new UpdateProductDTO();
        updateProductDTO.setName("Updated Gainer");
        updateProductDTO.setCategory("Updated Category");
        updateProductDTO.setBrand("Updated Brand");
        updateProductDTO.setPrice(20.99);
        updateProductDTO.setNewImage(null); // Без обновления изображения

        // Вызываем метод обновления
        productsService.updateProduct(savedProduct.getId(), updateProductDTO);

        // Проверяем обновленный продукт
        ProductsEntity updatedProduct = productRepository.findById(savedProduct.getId())
                .orElseThrow(() -> new ProductNotFoundException("Продукт не найден"));

        assertEquals("Updated Gainer", updatedProduct.getName());
        assertEquals("Updated Category", updatedProduct.getCategory());
        assertEquals("Updated Brand", updatedProduct.getBrand());
        assertEquals(20.99, updatedProduct.getPrice());
        assertNull(updatedProduct.getImagePath());
    }

}

























