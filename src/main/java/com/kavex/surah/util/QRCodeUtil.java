package com.kavex.surah.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class QRCodeUtil {

    /**
     * Gera um QR Code como BufferedImage.
     *
     * @param text O texto para codificar no QR Code.
     * @param width Largura do QR Code.
     * @param height Altura do QR Code.
     * @return BufferedImage representando o QR Code.
     */
    public static BufferedImage generateQRCode(String text, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        var bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * Adiciona um QR Code ao canto inferior direito de uma imagem.
     *
     * @param originalImage A imagem original onde o QR Code será adicionado.
     * @param qrCodeText O texto a ser codificado no QR Code.
     * @param qrCodeSize Tamanho do QR Code.
     * @return BufferedImage com o QR Code sobreposto.
     */
    public static BufferedImage addQRCodeToImage(BufferedImage originalImage, String qrCodeText, int qrCodeSize) throws WriterException, IOException {
        BufferedImage qrCode = generateQRCode(qrCodeText, qrCodeSize, qrCodeSize);

        // Criar uma cópia da imagem original
        BufferedImage combinedImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = combinedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, null);

        // Posicionar o QR Code no canto inferior direito
        int x = originalImage.getWidth() - qrCode.getWidth() - 10;
        int y = originalImage.getHeight() - qrCode.getHeight() - 10;
        g2d.drawImage(qrCode, x, y, null);

        g2d.dispose();
        return combinedImage;
    }
}
