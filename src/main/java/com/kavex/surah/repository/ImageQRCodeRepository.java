package com.kavex.surah.repository;

import com.kavex.surah.model.ImageQRCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageQRCodeRepository extends JpaRepository<ImageQRCode, Long> {
    Optional<ImageQRCode> findByQrCodeData(String qrCodeData);
}
