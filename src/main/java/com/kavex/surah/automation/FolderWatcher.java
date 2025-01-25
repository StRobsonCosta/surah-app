package com.kavex.surah.automation;

import java.nio.file.*;

public class FolderWatcher {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("caminho/da/pasta");

        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        System.out.println("Monitorando a pasta: " + path);

        while (true) {
            WatchKey key = watchService.take();

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    Path fileName = (Path) event.context();
                    System.out.println("Novo arquivo detectado: " + fileName);

                    // Chamar o método para processar a foto e gerar o QR Code
                }
            }

            key.reset();
        }
    }
}
