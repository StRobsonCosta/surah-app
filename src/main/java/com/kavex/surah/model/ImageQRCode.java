package com.kavex.surah.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageQRCode {


    private Long id;

    private String qrCodeData;

    private String imageUrl;

    public ImageQRCode(String qrCodeData, String imageUrl) {
        this.qrCodeData = qrCodeData;
        this.imageUrl = imageUrl;
    }

}
