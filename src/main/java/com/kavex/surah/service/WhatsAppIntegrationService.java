package com.kavex.surah.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppIntegrationService {

    @Value("${whatsapp.api.url}")
    private String whatsappApiUrl;

    @Value("${whatsapp.api.token}")
    private String whatsappApiToken;


    public void sendImage(String phoneNumber, String imageUrl, String caption) {
        RestTemplate restTemplate = new RestTemplate();

        // WhatsApp API payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("messaging_product", "whatsapp");
        payload.put("to", phoneNumber);
        payload.put("type", "image");

        Map<String, String> imageData = new HashMap<>();
        imageData.put("link", imageUrl);
        payload.put("image", imageData);

        if (caption != null) {
            imageData.put("caption", caption);
        }

        // HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(whatsappApiToken);

        // HTTP request
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(whatsappApiUrl, request, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send image via WhatsApp: " + e.getMessage(), e);
        }
    }

}
