package com.kavex.surah.api.controllers;

import com.kavex.surah.integration.WhatsAppService;
import com.kavex.surah.service.QRCodeService;
import com.kavex.surah.service.WhatsAppIntegrationService;
import com.kavex.surah.util.QRCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/qrcode")
@RequiredArgsConstructor
public class QRCodeController {

    private final QRCodeService qrCodeService;
    private final WhatsAppIntegrationService whatsAppIntegrationService;
    private final WhatsAppService whatsAppService;

    @Value("${app.image.upload-dir}")
    private String uploadDir;

    @Value("${app.image.monitoring-dir}")
    private String downloadDir;

    @PostMapping("/generate")
    public ResponseEntity<?> generateQRCodeWithImage(@RequestParam String qrCodeData, @RequestParam String imagePath) {
        try {
            // Carregar a imagem original
            BufferedImage originalImage = ImageIO.read(new File(imagePath));

            // Gerar a imagem com o QR Code embutido
            BufferedImage imageWithQRCode = QRCodeUtil.addQRCodeToImage(originalImage, qrCodeData, 100);

            // Salvar a nova imagem
            String outputPath = uploadDir + "imageName";
            ImageIO.write(imageWithQRCode, "png", new File(outputPath));

            return ResponseEntity.ok("QR Code generated and saved at: " + outputPath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating QR Code: " + e.getMessage());
        }
    }

//    @GetMapping("/image/{filename}")
//    public ResponseEntity<?> getImage(@PathVariable String filename) {
//        try {
//            Path imagePath = Paths.get("uploads", filename);
//            Resource resource = new UrlResource(imagePath.toUri());
//
//            if (!resource.exists() || !resource.isReadable()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
//            }
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_PNG)
//                    .body(resource);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving image: " + e.getMessage());
//        }
//    }

    @GetMapping("/images/{fileName}")
    public ResponseEntity<?> getImage(@PathVariable String fileName) {
        Path path = Paths.get("uploads/" + fileName);
        Resource resource = new FileSystemResource(path.toFile());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }


//    @PostMapping("/scanning")
//    public ResponseEntity<?> findImageByQRCode(@RequestParam String qrCodeData) {
//        if (qrCodeData == null || qrCodeData.isBlank()) {
//            return ResponseEntity.badRequest().body("Invalid QR Code data");
//        }
//
//        return qrCodeService.findImageByQRCode(qrCodeData)
//                .map(imageQRCode -> ResponseEntity.ok(imageQRCode.getImageUrl()))
//                .orElseGet(() -> ResponseEntity.status(404).body("QR Code not found"));
//    }

    @PostMapping("/sendImage")
    public ResponseEntity<?> sendImageByQRCode(
            @RequestParam String qrCodeData,
            @RequestParam String phoneNumber) {

        if (qrCodeData == null || qrCodeData.isBlank()) {
            return ResponseEntity.badRequest().body("Invalid QR Code data");
        }

        // Constrói o caminho da imagem original
        String imagePath = downloadDir + qrCodeData;
        File imageFile = new File(imagePath);

        if (!imageFile.exists()) {
            return ResponseEntity.status(404).body("Imagem não encontrada");
        }

        // Envia a imagem pelo WhatsApp
        whatsAppService.sendImage(phoneNumber, imageFile);

        return ResponseEntity.ok("Imagem enviada para " + phoneNumber);
    }


    @PostMapping("/download")
    public ResponseEntity<?> downloadViaWhatsApp( // para Cenarios AWS
            @RequestParam String qrCodeData,
            @RequestParam String phoneNumber) {
        if (qrCodeData == null || qrCodeData.isBlank()) {
            return ResponseEntity.badRequest().body("Invalid QR Code data");
        }

        return qrCodeService.findImageByQRCode(qrCodeData)
                .map(imageQRCode -> {
                    whatsAppService.sendImage(phoneNumber, imageQRCode.getImageUrl());
                    return ResponseEntity.ok("Image sent to " + phoneNumber);
                })
                .orElseGet(() -> ResponseEntity.status(404).body("QR Code not found"));
    }
}
