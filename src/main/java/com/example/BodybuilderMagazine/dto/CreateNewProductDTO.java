package com.example.BodybuilderMagazine.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateNewProductDTO extends ProductDTO{


    @NotBlank(message = "Фото должно быть обязательно")
    private MultipartFile image;


}
