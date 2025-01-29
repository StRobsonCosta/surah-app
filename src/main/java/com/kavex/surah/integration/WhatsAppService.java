package com.kavex.surah.integration;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WhatsAppService {

    @Value("${app.zap.account-sid}")
    private String accountSid;

    @Value("${app.zap.auth-token}")
    private String authToken;

    @Value("${app.zap.number}")
    private String zapNumber;

    public void sendImage(String toWhatsAppNumber, String imageUrl) {
        Twilio.init(accountSid, authToken);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("whatsapp:" + toWhatsAppNumber),
                        new com.twilio.type.PhoneNumber("whatsapp:" + zapNumber),
                        "Aqui está a imagem que você escaneou!")
                .setMediaUrl(List.of(URI.create(imageUrl)))
                .create();
        System.out.println("Mensagem enviada: " + message.getSid());
    }

    public void sendImage(String toWhatsAppNumber, File imageFile) {
        try {
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            String mimeType = Files.probeContentType(imageFile.toPath());

            // Twilio aceita envio de imagens via URL, então é necessário hospedar a imagem ou usar um workaround
            String dataUrl = "data:" + mimeType + ";base64," + base64Image;

            Twilio.init(accountSid, authToken);
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber("whatsapp:" + toWhatsAppNumber),
                            new com.twilio.type.PhoneNumber("whatsapp:" + zapNumber),
                            "Aqui está a imagem que você escaneou!")
                    .setMediaUrl(List.of(URI.create(dataUrl))) // Envia a imagem como Base64
                    .create();

            System.out.println("Mensagem enviada: " + message.getSid());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao ler a imagem para envio pelo WhatsApp");
        }
    }

}
