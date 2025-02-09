package com.kavex.surah.model;

import lombok.Data;

@Data
public class Photo {

    private String id;
    private String fileName;
    private String qrCode;
    private String downloadUrl;
}
