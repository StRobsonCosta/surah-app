package com.kavex.surah.service;

import com.google.zxing.WriterException;
import com.kavex.surah.service.interfaces.CloudStorageService;
import com.kavex.surah.util.QRCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${app.image.upload-dir}")
    private String uploadDir;

    @Value("${file.storage.type:local}")
    private String storageType;

    @Autowired
    private CloudStorageService cloudStorageService;

    public String uploadImage(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("File is empty or has an invalid name.");
        }

        String fileName = file.getOriginalFilename();
        String qrFileName = "qr_" + fileName;

        try {
            if ("local".equalsIgnoreCase(storageType)) {
                return handleLocalStorage(file, fileName, qrFileName);
            } else if ("cloud".equalsIgnoreCase(storageType)) {
                return handleCloudStorage(file, fileName, qrFileName);
            } else {
                throw new IllegalArgumentException("Invalid storage type: " + storageType);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    private String handleLocalStorage(MultipartFile file, String fileName, String qrFileName) throws IOException, WriterException {
        // Salvar imagem original localmente
        Path originalPath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), originalPath, StandardCopyOption.REPLACE_EXISTING);

        // Gerar QR Code e adicionar à imagem
        BufferedImage originalImage = ImageIO.read(originalPath.toFile());
        String qrText = "http://localhost:8080/uploads/" + fileName;
        BufferedImage imageWithQRCode = QRCodeUtil.addQRCodeToImage(originalImage, qrText, 100);

        // Salvar a imagem com QR Code localmente
        Path qrPath = Paths.get(uploadDir, qrFileName);
        ImageIO.write(imageWithQRCode, "png", qrPath.toFile());

        return "http://localhost:8080/uploads/" + qrFileName;
    }

    private String handleCloudStorage(MultipartFile file, String fileName, String qrFileName) throws IOException, WriterException {
        // Upload da imagem original para a nuvem
        String originalFileUrl = cloudStorageService.uploadFile(file.getInputStream(), fileName);

        // Gerar QR Code baseado na URL da imagem na nuvem
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage imageWithQRCode = QRCodeUtil.addQRCodeToImage(originalImage, originalFileUrl, 100);

        // Fazer upload da imagem com QR Code para a nuvem
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(imageWithQRCode, "png", outputStream);
        InputStream qrImageInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        return cloudStorageService.uploadFile(qrImageInputStream, qrFileName);
    }

    public List<String> getImagesWithQRCode() {
        File folder = new File(uploadDir);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalStateException("Upload directory does not exist: " + uploadDir);
        }

        List<String> urls = new ArrayList<>();
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg"));

        if (files == null) {
            return urls;
        }

        for (File file : files) {
            try {
                String qrFilePath = uploadDir + "/qr_" + file.getName();
                urls.add("http://localhost:8080/uploads/qr_" + file.getName());
            } catch (Exception e) {
                // Registrar erro ao processar arquivo específico
                System.err.println("Error processing file: " + file.getName() + " - " + e.getMessage());
            }
        }
        return urls;
    }
}

