package com.example.BodybuilderMagazine.controller;

import com.example.BodybuilderMagazine.entity.Product;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import com.example.BodybuilderMagazine.dto.CreateNewProductDTO;
import com.example.BodybuilderMagazine.dto.UpdateProductDTO;
import com.example.BodybuilderMagazine.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productsService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/")
    private String getAllProducts(Model model) {
        logger.info("Общая страница продуктов");
        model.addAttribute("products", productsService.findAll());
        return "product";
    }

    @GetMapping("/{id}")
    private String getProductById(@PathVariable("id") Long id, Model model) {
        logger.info("Сейчас видим продукт с ID: {},", id);
        Product product = productsService.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт с ID " + id + " не найден"));
        model.addAttribute("product", product);
        return "productDetails";
    }


    @GetMapping("/new")
    public String showCreateProductForm(Model model) {
        logger.info("Запрос на отображение формы для создания нового продукта ");
        model.addAttribute("product", new CreateNewProductDTO());
        return "new";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        logger.info("Запрос на редактирование продукта с ID: {}", id);
        Product product = productsService.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт с ID " + id + " не найден"));
        model.addAttribute("product", product);
        return "edit";
    }

    @PostMapping
    public String createNewProduct(@ModelAttribute("product") CreateNewProductDTO createNewProductDTO) {
        logger.info("Запрос на создание нового продукта");
        productsService.saveProduct(createNewProductDTO);
        return "redirect:/products/";
    }


    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute("product") UpdateProductDTO updateProductDTO) {
        logger.info("Запрос на обновление продукта с ID: {}", id);
        productsService.updateProduct(id, updateProductDTO);
        return "redirect:/products/";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String deleteProductById(@PathVariable("id") Long id) {
        logger.info("Запрос на удаление продукта с ID: {}", id);
        productsService.delete(id);
        return "redirect:/products/";
    }

    @GetMapping("/search")
    public String searchByNameProduct(
            @RequestParam(required = false) String name, Model model) {
        logger.info("Запрос на просмотр продукта по имени: {}", name);
        Product result = productsService.searchByName(name).stream().findFirst().orElse(null);
        model.addAttribute("product", result);
        return "productDetails";
    }


}






















