package com.kavex.surah.service;

import com.google.zxing.WriterException;
import com.kavex.surah.model.ImageQRCode;
import com.kavex.surah.repository.ImageQRCodeRepository;
import com.kavex.surah.util.QRCodeUtil;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

@Service
public class QRCodeService {

    private final ImageQRCodeRepository imageQRCodeRepository;

    public QRCodeService(ImageQRCodeRepository imageQRCodeRepository) {
        this.imageQRCodeRepository = imageQRCodeRepository;
    }

    public Optional<ImageQRCode> findImageByQRCode(String qrCodeData) {
        return imageQRCodeRepository.findByQrCodeData(qrCodeData);
    }

    public void saveQRCodeAssociation(String qrCodeData, String imageUrl) {
        if (!imageQRCodeRepository.findByQrCodeData(qrCodeData).isPresent()) {
            imageQRCodeRepository.save(new ImageQRCode(qrCodeData, imageUrl));
        }
    }

    public BufferedImage createQRCode(String text) throws WriterException {
        return QRCodeUtil.generateQRCode(text, 300, 300);
    }

    public BufferedImage addQRCodeToImage(BufferedImage originalImage, String text) throws WriterException, IOException {
        return QRCodeUtil.addQRCodeToImage(originalImage, text, 100);
    }
}
