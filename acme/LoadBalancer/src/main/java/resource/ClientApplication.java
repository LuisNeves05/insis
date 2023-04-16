package resource;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(ClientApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        WebClient loadBalancedClient = ctx.getBean(WebClient.Builder.class).build();

    }
}