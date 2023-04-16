package resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReviewServiceWriteApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReviewServiceWriteApplication.class, args);
    }

    @Value("${server.instance.id}")
    String instanceId;
}