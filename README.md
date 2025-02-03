# Documentação do Projeto - Monitoramento de Imagens e Envio via WhatsApp

## Visão Geral
Este projeto tem como objetivo monitorar um diretório local, detectar novas imagens, gerar um QR Code embutido na imagem e permitir que, ao escanear o QR Code, a imagem original seja enviada via WhatsApp utilizando a API do Twilio.

## Funcionalidades Principais
- **Monitoramento de Diretório**: Observa a pasta `/home/Imagens/Monitoramento` e detecta novas imagens.
- **Geração de QR Code**: Adiciona um QR Code à cópia da imagem salva em `/home/Imagens/QRCoded`.
- **Serviço de Envio via WhatsApp**: Envia a imagem original via WhatsApp ao escanear o QR Code.

## Tecnologias Utilizadas
- **Java 17+**: Linguagem principal para desenvolvimento.
- **Spring Boot**: Framework para facilitar o desenvolvimento do backend.
- **Twilio API**: API para envio de mensagens e imagens via WhatsApp.
- **ZXing**: Biblioteca para geração de QR Codes.
- **PostgreSQL / MongoDB**: Opções de banco de dados para armazenar informações.
- **Docker**: Para facilitar a execução de serviços como MongoDB.

## Estrutura do Projeto
```
/projeto-root
│── src/main/java/com/seuprojeto
│   ├── FolderWatcher.java          # Serviço de monitoramento de diretório
│   ├── QRCodeService.java          # Serviço para geração de QR Codes
│   ├── WhatsAppService.java        # Serviço para envio de imagens via WhatsApp
│   ├── Controller.java             # API para integração com frontend e QR Code Scanner
│── resources/application.properties # Configuração do projeto
│── uploads/                        # Pasta onde as imagens são armazenadas
│── qrcoded/                         # Pasta onde as imagens com QR Codes são armazenadas
```

## Como Funciona
1. Uma nova imagem é salva na pasta `/home/Imagens/Monitoramento`.
2. O serviço `FolderWatcher` detecta a nova imagem.
3. Um QR Code é gerado contendo um identificador único ou URL.
4. A cópia com o QR Code é salva em `/home/Imagens/QRCoded`.
5. Quando um usuário escaneia o QR Code, uma requisição é feita para obter a imagem original.
6. O serviço `WhatsAppService` usa o Twilio para enviar a imagem original ao número associado.

## Exemplo de QR Code
O QR Code gerado pode conter:
```
/sendImage?qrCodeData=swift-bruxo.jpeg&phoneNumber=+551199999999
```
Ou apenas:
```
swift-bruxo.jpeg
```

## Testando no Postman
1. Simular o escaneamento do QR Code:
   - Fazer uma requisição **GET**:
   ```
   GET http://localhost:8080/sendImage?qrCodeData=swift-bruxo.jpeg&phoneNumber=+551199999999
   ```
2. Se tudo estiver correto, a imagem original será enviada via WhatsApp para o número indicado.

## Considerações Finais
Este projeto pode ser expandido para suportar armazenamento em nuvem, envio de imagens via outras plataformas e integração com serviços de autenticação.
