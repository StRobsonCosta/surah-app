package com.kavex.surah.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppWebService {

    @Value("${chrome.driver.path}")
    private String chromeDriverPath;


    private static final String DRIVER_PATH = "C:/caminho/para/chromedriver.exe";// definir com injeção chromeDriverPath

    public void sendMessage(String contactName, String message) {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://web.whatsapp.com");
            System.out.println("Escaneie o código QR para fazer login...");

            Thread.sleep(30000); // Tempo para login manual

            WebElement searchBox = driver.findElement(By.xpath("//div[@contenteditable='true'][@data-tab='3']"));
            searchBox.sendKeys(contactName);
            Thread.sleep(2000);

            WebElement contact = driver.findElement(By.xpath("//span[@title='" + contactName + "']"));
            contact.click();

            WebElement messageBox = driver.findElement(By.xpath("//div[@contenteditable='true'][@data-tab='10']"));
            messageBox.sendKeys(message);

            WebElement sendButton = driver.findElement(By.xpath("//button[@data-testid='compose-btn-send']"));
            sendButton.click();

            System.out.println("Mensagem enviada para: " + contactName);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar mensagem pelo WhatsApp Web", e);
        } finally {
            driver.quit();
        }
    }
}
