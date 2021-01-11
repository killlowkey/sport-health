package com.xzc.sport.health;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.http.MediaType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.LockSupport;

/**
 * https://stackoverflow.com/questions/36464682/adding-servlet-to-embedded-tomcat
 *
 * @author Ray
 * @date created in 2021/1/8 20:47
 */
public class TomcatTest {
    @Test
    public void tomcat_start() throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        String servletName = "dispatcherServlet";
        String contextPath = "/";
        String docBase = new File(".").getAbsolutePath();

        Context context = tomcat.addContext(contextPath, docBase);
        tomcat.addServlet(contextPath, servletName, new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException,
                    IOException {
                response.setStatus(200);
                response.setContentType(MediaType.TEXT_HTML_VALUE);
                response.getOutputStream().write("hello world".getBytes());
            }
        });
        context.addServletMappingDecoded("/", servletName);
        tomcat.start();

        LockSupport.park(Thread.currentThread());
    }

    @Test
    void spring_boot_tomcat_start() {
        TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        TomcatWebServer webServer = (TomcatWebServer) serverFactory.getWebServer();

        Tomcat tomcat = webServer.getTomcat();
        String servletName = "dispatcherServlet";
        String contextPath = "/";
        String docBase = new File(".").getAbsolutePath();

        Context context = tomcat.addContext(contextPath, docBase);
        tomcat.addServlet(contextPath, servletName, new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException,
                    IOException {
                response.setStatus(200);
                response.setContentType(MediaType.TEXT_HTML_VALUE);
                response.getOutputStream().write("hello world".getBytes());
            }
        });
        context.addServletMappingDecoded("/", servletName);
        webServer.start();

        LockSupport.park(Thread.currentThread());
    }
}
