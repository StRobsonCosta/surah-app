package com.kavex.surah.service;

import com.google.zxing.WriterException;
import com.kavex.surah.util.QRCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    public BufferedImage createQRCode(String text) throws WriterException {
        return QRCodeUtil.generateQRCode(text, 300, 300);
    }

    public BufferedImage addQRCodeToImage(BufferedImage originalImage, String text) throws WriterException, IOException {
        return QRCodeUtil.addQRCodeToImage(originalImage, text, 100);
    }
}
