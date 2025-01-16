package com.example.BodybuilderMagazine.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import com.example.BodybuilderMagazine.dto.CreateNewProductDTO;
import com.example.BodybuilderMagazine.dto.UpdateProductDTO;
import com.example.BodybuilderMagazine.entity.ProductsEntity;
import com.example.BodybuilderMagazine.services.ProductsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Tag(name = "PeopleController")
@CrossOrigin(origins = "http://localhost:8081")
@Controller
@RequestMapping("/products")
public class ProductsController {

    private final ProductsService productsService;

    private static final Logger logger = LoggerFactory.getLogger(ProductsController.class);


    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }


    @Operation(summary = "Получение всех людей")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = ProductsEntity.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/")
    private String getAllProducts(Model model) {
        logger.info("Сейчас на общей странице всех продуктов");
        return productsService.findAll(model);
    }

    @GetMapping("/{id}")
    private String getProductById(@PathVariable("id") int id, Model model) {
        logger.info("Сейчас видим продукт с ID: {},", id);
        return productsService.findById(model, id);
    }

    @GetMapping("/new")
    public String showCreateProductForm(Model model) {
        logger.info("Запрос на отображение формы для создания нового продукта ");
        model.addAttribute("product", new CreateNewProductDTO());
        return "new";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") int id, Model model) {
        logger.info("Запрос для просмотра редактированного продукта с ID: {}", id);
        productsService.showEditProductForm(id, model);
        return "edit";
    }

    @PostMapping
    public String createNewProduct(@ModelAttribute("product") CreateNewProductDTO createNewProductDTO) {
        logger.info("Запрос на создание нового продукта");
        productsService.saveProduct(createNewProductDTO);
        return "redirect:/products/";
    }


    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") int id, @ModelAttribute("product") UpdateProductDTO updateProductDTO) {
        logger.info("Запрос на обновление продукта с ID: {}", id);
        productsService.updateProduct(id, updateProductDTO);
        return "redirect:/products/";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String deleteProductById(@PathVariable("id") int id) {
        logger.info("Запрос на удаление продукта с ID: {}", id);
        productsService.deleteProductById(id);
        return "redirect:/products/";
    }

    @GetMapping("/search")
    public String searchByNameProduct(
            @RequestParam(required = false) String name, Model model) {
        logger.info("Запрос на просмотр продукта по названию: {}", name);
        ProductsEntity result = productsService.searchByName(name).stream().findFirst().orElse(null);
        model.addAttribute("product", result);
        return "productDetails";
    }


}






















