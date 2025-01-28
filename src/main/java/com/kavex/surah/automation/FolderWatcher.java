package com.kavex.surah.automation;

import com.google.zxing.WriterException;
import com.kavex.surah.service.QRCodeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
@RequiredArgsConstructor
public class FolderWatcher {

    @Value("${app.image.monitoring-dir}")
    private String monitoringDir;

    @Value("${app.image.upload-dir}")
    private String qrCodedImage;

    @Autowired
    private QRCodeService qrCodeService;

    @PostConstruct
    public void startMonitoring() {
        new Thread(() -> {
            try {
                Path path = Paths.get(monitoringDir);
                WatchService watchService = FileSystems.getDefault().newWatchService();
                path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

                System.out.println("Monitorando a pasta: " + path);

                while (true) {
                    WatchKey key = watchService.take();

                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                            Path fileName = (Path) event.context();
                            System.out.println("Novo arquivo detectado: " + fileName);

                            // Chamar o método para processar a foto e gerar o QR Code
                            processFile(path.resolve(fileName), fileName);
                        }
                    }

                    if (!key.reset()) {
                        System.out.println("Monitoramento encerrado.");
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro durante o monitoramento: " + e.getMessage());
            }
        }).start();
    }

    private void processFile(Path filePath, Path fileName) throws IOException, WriterException {
        System.out.println("Processando arquivo: " + filePath);
        BufferedImage originalImage = ImageIO.read(new File(filePath.toString()));

        // Gera a imagem com QRCode
        System.out.println("Adicionando QRCode na Imagem");
        BufferedImage imageWithQRCode = qrCodeService.addQRCodeToImage(originalImage, "teste de geração QRCode");

        String outputPath = qrCodedImage + fileName;
        // Verifica se o diretório existe e cria, se necessário
        Files.createDirectories(Paths.get(qrCodedImage));

        // Salva a imagem
        ImageIO.write(imageWithQRCode, "png", new File(outputPath));

        System.out.println("Imagem com QRCode salva em: " + outputPath);
    }
}