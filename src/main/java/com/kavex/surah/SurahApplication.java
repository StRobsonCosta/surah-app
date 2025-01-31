package com.kavex.surah;

import com.kavex.surah.integration.LocalImageServer;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SurahApplication {

	public static void main(String[] args) throws Exception {
		Dotenv dotenv = Dotenv.load();

		for (DotenvEntry entry : dotenv.entries())
			System.setProperty(entry.getKey(), entry.getValue());
		SpringApplication.run(SurahApplication.class, args);

		LocalImageServer.startServer(8082, "/home/robson/Imagens/Monitoramento/");
	}

}
