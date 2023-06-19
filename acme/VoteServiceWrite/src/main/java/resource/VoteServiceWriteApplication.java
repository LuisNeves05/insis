package resource;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class VoteServiceWriteApplication {
    public static void main(String[] args) {
        SpringApplication.run(VoteServiceWriteApplication.class, args);
    }
}