package com.kavex.surah.integration;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
}
