package com.kavex.surah.api.controllers;

import com.kavex.surah.service.QRCodeService;
import com.kavex.surah.service.WhatsAppIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qrcode")
@RequiredArgsConstructor
public class QRCodeController {

    private final QRCodeService qrCodeService;
    private final WhatsAppIntegrationService whatsAppIntegrationService;

//    public QRCodeController(QRCodeService qrCodeService, WhatsAppIntegrationService whatsAppIntegrationService) {
//        this.qrCodeService = qrCodeService;
//        this.whatsAppIntegrationService = whatsAppIntegrationService;
//    }

    @PostMapping("/scanning")
    public ResponseEntity<?> processScannedQRCode(@RequestParam String qrCodeData) {
        if (qrCodeData == null || qrCodeData.isBlank()) {
            return ResponseEntity.badRequest().body("Invalid QR Code data");
        }

        return qrCodeService.findImageByQRCode(qrCodeData)
                .map(imageQRCode -> ResponseEntity.ok(imageQRCode.getImageUrl()))
                .orElseGet(() -> ResponseEntity.status(404).body("QR Code not found"));
    }

    @PostMapping("/scan")
    public ResponseEntity<?> processScannedQRCode(@RequestParam String qrCodeData, @RequestParam String phoneNumber) {
        if (qrCodeData == null || qrCodeData.isBlank()) {
            return ResponseEntity.badRequest().body("Invalid QR Code data");
        }

        return qrCodeService.findImageByQRCode(qrCodeData)
                .map(imageQRCode -> {
                    // Send image via WhatsApp
                    whatsAppIntegrationService.sendImage(phoneNumber, imageQRCode.getImageUrl(), "Here is your photo!");

                    return ResponseEntity.ok("Image sent to " + phoneNumber);
                })
                .orElseGet(() -> ResponseEntity.status(404).body("QR Code not found"));
    }
}
