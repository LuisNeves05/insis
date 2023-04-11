package resource;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class ProductServiceWriteApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceWriteApplication.class, args);
    }
}