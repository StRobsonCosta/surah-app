package com.kavex.surah.integration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class LocalImageServer {

    public static void startServer(int port, String directory) throws Exception {
        Server server = new Server(port);

        // Configura o servidor para servir arquivos da pasta especificada
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setWelcomeFiles(new String[]{"index.html"}); // Define página inicial, se necessário
        resourceHandler.setDirAllowed(true);

        // Define o contexto raiz "/"
        ContextHandler contextHandler = new ContextHandler("/");
        contextHandler.setHandler(resourceHandler);
//        contextHandler.setBaseResource(org.eclipse.jetty.util.resource.Resource.newResource(directory));

        server.setHandler(contextHandler);
        server.start();

        System.out.println("Servidor local iniciado em: http://localhost:" + port);
    }
}
