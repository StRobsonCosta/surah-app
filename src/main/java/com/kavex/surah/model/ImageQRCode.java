package com.kavex.surah.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageQRCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String qrCodeData;

    @Column(nullable = false)
    private String imageUrl;

    public ImageQRCode(String qrCodeData, String imageUrl) {
        this.qrCodeData = qrCodeData;
        this.imageUrl = imageUrl;
    }

}
