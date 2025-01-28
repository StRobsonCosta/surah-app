package com.kavex.surah.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;

public class WhatsAppWebAutomation {

    @Value("${app.chromedriver.path.exe}")
    private String chromedriver;

    public static void main(String[] args) throws InterruptedException {
        // Configurar o caminho do ChromeDriver
        System.setProperty("webdriver.chrome.driver", "/home/robson/chrome-driver/chromedriver");

        // Inicializar o WebDriver
        WebDriver driver = new ChromeDriver();

        try {
            // Acessar o WhatsApp Web
            driver.get("https://web.whatsapp.com");
            System.out.println("Escaneie o código QR para fazer login...");

            // Aguardar login manual (30 segundos para escanear o QR Code)
            Thread.sleep(30000);

            // Procurar pelo contato ou grupo
            String contato = "Tem alguém no banho";
            WebElement searchBox = driver.findElement(By.xpath("//div[@contenteditable='true'][@data-tab='3']"));
            searchBox.sendKeys(contato);

            // Aguardar resultados da pesquisa
            Thread.sleep(2000);

            // Selecionar o contato
            WebElement contact = driver.findElement(By.xpath("//span[@title='" + contato + "']"));
            contact.click();

            // Enviar a mensagem
            String mensagem = "Olá, esta é uma mensagem de teste enviada automaticamente!";
            WebElement messageBox = driver.findElement(By.xpath("//div[@contenteditable='true'][@data-tab='10']"));
            messageBox.sendKeys(mensagem);

            // Clicar no botão de enviar
            WebElement sendButton = driver.findElement(By.xpath("//button[@data-testid='compose-btn-send']"));
            sendButton.click();

            System.out.println("Mensagem enviada para " + contato);

        } finally {
            // Fechar o navegador após o teste
            Thread.sleep(55000);
            driver.quit();
        }
    }
}
