package com.kavex.surah.integration;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;
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
//            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
//            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
//            String mimeType = Files.probeContentType(imageFile.toPath());
//
//            // Twilio aceita envio de imagens via URL, então é necessário hospedar a imagem ou usar um workaround
//            String dataUrl = "data:" + mimeType + ";base64," + base64Image;
//
//            String localServerUrl = "http://localhost:8082/" + imageFile;

            String localServerUrl = "https://0102-2804-5b30-2c35-a700-1fcf-1dbe-8450-a093.ngrok-free.app" + imageFile;

            Twilio.init(accountSid, authToken);
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber("whatsapp:" + toWhatsAppNumber),
                            new com.twilio.type.PhoneNumber("whatsapp:" + zapNumber),
                            "Aqui está a imagem que você escaneou!")
                    .setMediaUrl(List.of(URI.create(localServerUrl))) // Envia a imagem como Base64
                    .create();

            System.out.println("Mensagem enviada: " + message.getSid());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao ler a imagem para envio pelo WhatsApp");
        }
    }

}
