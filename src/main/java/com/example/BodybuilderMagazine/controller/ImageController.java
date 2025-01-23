package com.example.BodybuilderMagazine.controller;

import com.example.BodybuilderMagazine.entity.Image;
import com.example.BodybuilderMagazine.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable Long id) {
        Image imageEntity = imageService.getImageById(id);

        ByteArrayResource resource = new ByteArrayResource(imageEntity.getImage());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"image_" + id + ".jpg\"")
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(imageEntity.getImage().length)
                .body(resource);
    }
}
