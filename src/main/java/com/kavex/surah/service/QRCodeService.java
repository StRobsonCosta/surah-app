package com.kavex.surah.service;

import com.kavex.surah.model.ImageQRCode;
import com.kavex.surah.repository.ImageQRCodeRepository;
import org.springframework.stereotype.Service;

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
}
