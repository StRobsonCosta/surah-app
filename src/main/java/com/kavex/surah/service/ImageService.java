package com.kavex.surah.service;

import com.google.zxing.WriterException;
import com.kavex.surah.util.QRCodeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    @Value("${app.image.upload-dir}")
    private String uploadDir;

    @Value("${app.image.s3.bucket}")
    private String s3Bucket;

    public String uploadImageLocal(MultipartFile file) {
        try {
            // Salvar no servidor local
            String filePath = uploadDir + "/" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(filePath));
            return filePath;

            // Para upload no S3, substitua pela lógica de upload para AWS
            // String s3Url = uploadToS3(file);
            // return s3Url;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public String uploadImage(MultipartFile file) {
        try {
            // Salvar a imagem original no servidor local
            String originalFilePath = uploadDir + "/" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(originalFilePath));

            // Gerar o QR Code e adicioná-lo à imagem
            BufferedImage originalImage = ImageIO.read(new File(originalFilePath));
            String qrText = "https://example.com/view/" + file.getOriginalFilename();
            BufferedImage imageWithQRCode = QRCodeUtil.addQRCodeToImage(originalImage, qrText, 100);

            // Salvar a imagem com o QR Code
            String qrFilePath = uploadDir + "/qr_" + file.getOriginalFilename();
            ImageIO.write(imageWithQRCode, "png", new File(qrFilePath));

            return "http://localhost:8080/uploads/qr_" + file.getOriginalFilename();
        } catch (IOException | RuntimeException | WriterException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public List<String> getImagesWithQRCode() {
        List<String> urls = new ArrayList<>();
        File folder = new File(uploadDir);

        for (File file : folder.listFiles()) {
            try {
                // Adicionar QR Code
                BufferedImage image = ImageIO.read(file);
                String qrText = "https://example.com/view/" + file.getName();
                BufferedImage qrCode = QRCodeUtil.generateQRCode(qrText);

                Graphics2D g2d = image.createGraphics();
                g2d.drawImage(qrCode, image.getWidth() - qrCode.getWidth() - 10, 10, null);
                g2d.dispose();

                // Salvar imagem com QR Code
                String qrFilePath = uploadDir + "/qr_" + file.getName();
                ImageIO.write(image, "png", new File(qrFilePath));

                // Adicionar URL da imagem gerada
                urls.add("http://localhost:8080/uploads/qr_" + file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return urls;
    }
}
