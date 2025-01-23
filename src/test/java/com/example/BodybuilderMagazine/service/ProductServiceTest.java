package com.example.BodybuilderMagazine.service;

import com.example.BodybuilderMagazine.dto.CreateNewProductDTO;
import com.example.BodybuilderMagazine.dto.UpdateProductDTO;
import com.example.BodybuilderMagazine.entity.Product;
import com.example.BodybuilderMagazine.repository.ProductRepository;
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
    private ProductService productsService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MultipartFile mockImage;

    @Mock
    private Model model;

    private CreateNewProductDTO createNewProductDTO;

    private List<Product> mockProducts;

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

        verify(productRepository, times(1)).save(any(Product.class));

        Path imagePath = uploadPath.resolve("testImage.jpg");
        assertTrue(Files.exists(imagePath));
    }


    @Test
    void findAll() {

        Product product1 = new Product();
        product1.setName("Protein");

        Product product2 = new Product();
        product2.setName("L-Carnitin");

        List<Product> productsEntityList = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(productsEntityList);

        Model model = mock(Model.class);

        String viewName = productsService.findAll().toString();

        verify(productRepository, times(1)).findAll();

        verify(model).addAttribute("products", productsEntityList);

        assertEquals("products", viewName);
    }

    @Test
    void findById() {
        Product product1 = new Product();
        product1.setId(1);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        Optional<Product> result = productRepository.findById(1L);

        Assertions.assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void updateProduct() {

        CreateNewProductDTO createNewProductDTO = new CreateNewProductDTO();
        long productId = 1L;
        createNewProductDTO.setId((long) productId);
        createNewProductDTO.setName("Gainer");
        createNewProductDTO.setCategory("Gainers");
        createNewProductDTO.setDescriptions("Super Gainer");
        createNewProductDTO.setPrice(15.99);
        createNewProductDTO.setBrand("5LB");
        createNewProductDTO.setImage(null);

        Product savedProduct = productsService.saveProduct(createNewProductDTO);

        // Создаем DTO для обновления
        UpdateProductDTO updateProductDTO = new UpdateProductDTO();
        updateProductDTO.setName("Updated Gainer");
        updateProductDTO.setCategory("Updated Category");
        updateProductDTO.setBrand("Updated Brand");
        updateProductDTO.setPrice(20.99);
        updateProductDTO.setNewImage(null); // Без обновления изображения

        // Вызываем метод обновления
        productsService.updateProduct((long) savedProduct.getId(), updateProductDTO);

        // Проверяем обновленный продукт
        Product updatedProduct = productRepository.findById((long) savedProduct.getId())
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));

        assertEquals("Updated Gainer", updatedProduct.getName());
        assertEquals("Updated Category", updatedProduct.getCategory());
        assertEquals("Updated Brand", updatedProduct.getBrand());
        assertEquals(20.99, updatedProduct.getPrice());
        assertNull(updatedProduct.getImage());
    }

    @Test
    void deleteProduct() {

        int productId = 1;
        Product products = new Product();
        products.setId(productId);
        products.setName("Test product");

        when(productRepository.findById((long) productId)).thenReturn(Optional.of(products));

        productsService.delete((long) productId);

        verify(productRepository).deleteById((long) productId);
    }

    @Test
    void showEditProductForm_ProductExist() {
        int productId = 4;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test product");


        when(productRepository.findById((long) productId)).thenReturn(Optional.of(product));

        productsService.showEditProductForm((long) productId, model);

        verify(model, times(1)).addAttribute("product", product);
    }

    @Test
    void searchByNameTest() {

        String productName = "Protein";

        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Protein");

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Protein Plus");

        List<Product> productList = List.of(product1, product2);

        when(productRepository.findByName(productName)).thenReturn(productList);

        List<Product> result = productsService.searchByName(productName);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Protein", result.get(0).getName());
        assertEquals("Protein Plus", result.get(1).getName());

        // Проверяем вызов метода репозитория
        verify(productRepository, times(1)).findByName(productName);
    }

}

























