package com.kavex.surah.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "photos")
public class Photo {

    @Id
    private String id;
    private String fileName;
    private String qrCode;
    private String downloadUrl;
}
