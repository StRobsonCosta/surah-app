package com.kavex.surah.service.interfaces;

import java.io.InputStream;

public interface CloudStorageService {
    String uploadFile(InputStream inputStream, String fileName);
}
