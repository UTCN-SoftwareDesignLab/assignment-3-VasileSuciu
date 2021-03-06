package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@SpringBootApplication
@EntityScan("demo.model")
@EnableJpaRepositories({"demo.repository"})
@ComponentScan({"demo.database","demo.model","demo.service", "demo.controllers","demo.configuration", "demo.websocket"})
@PropertySource({"classpath:application.properties"})
public class DemoApplication {

	public static void main(String[] args) {
	    SpringApplication.run(DemoApplication.class, args);
	}

}
