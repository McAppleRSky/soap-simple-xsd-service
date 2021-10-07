package ru.mrs.ws.server.soap.simple.xsd;

// https://www.baeldung.com/spring-boot-soap-web-service
// https://spring-projects.ru/guides/producing-web-service/
// https://simplesolution.dev/gradle-configuration-to-generate-java-classes-from-wsdl-with-jaxb/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

//@ComponentScan
//@EnableAutoConfiguration
public class WsApplication {
    public static void main(String[] args) {
        SpringApplication.run(WsApplication.class, args);
    }
}
