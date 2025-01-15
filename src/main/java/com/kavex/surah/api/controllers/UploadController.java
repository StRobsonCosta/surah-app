package com.kavex.surah.api.controllers;

import com.kavex.surah.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class UploadController {

    @Autowired
    private ImageService imageService;

    // Endpoint para upload de imagem
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = imageService.uploadImage(file);
        return ResponseEntity.ok("Image uploaded successfully. Access it at: " + url);
    }

    // Endpoint para listar imagens com QR Codes
    @GetMapping
    public ResponseEntity<List<String>> listImages() {
        List<String> imageUrls = imageService.getImagesWithQRCode();
        return ResponseEntity.ok(imageUrls);
    }
}
