package com.kavex.surah.api.controllers;

import com.kavex.surah.service.QRCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qrcode")
public class QRCodeController {

    private final QRCodeService qrCodeService;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/scan")
    public ResponseEntity<?> processScannedQRCode(@RequestParam String qrCodeData) {
        if (qrCodeData == null || qrCodeData.isBlank()) {
            return ResponseEntity.badRequest().body("Invalid QR Code data");
        }

        return qrCodeService.findImageByQRCode(qrCodeData)
                .map(imageQRCode -> ResponseEntity.ok(imageQRCode.getImageUrl()))
                .orElseGet(() -> ResponseEntity.status(404).body("QR Code not found"));
    }
}
