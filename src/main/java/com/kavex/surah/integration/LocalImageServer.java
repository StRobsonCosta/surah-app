package com.kavex.surah.integration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import java.net.URI;
import java.nio.file.Path;


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
//        contextHandler.setBaseResource(Resource.dump(new Resource(directory) ));

        server.setHandler(contextHandler);
        server.start();

        System.out.println("Servidor local iniciado em: http://localhost:" + port);
    }
//    public static void startServer(int port, String directory) throws Exception {
//        Server server = new Server(port);
//
//        // Configura o contexto do servlet
//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//
//        // Configura o servlet para servir arquivos estáticos
//        ServletHolder defaultServlet = new ServletHolder("default", new org.eclipse.jetty.servlet.DefaultServlet());
//        defaultServlet.setInitParameter("resourceBase", directory);
//        defaultServlet.setInitParameter("dirAllowed", "true");
//
//        context.addServlet(defaultServlet, "/");
//
//        server.setHandler(context);
//        server.start();
//
//        System.out.println("Servidor local iniciado em: http://localhost:" + port);
//        server.join();
//    }

}
