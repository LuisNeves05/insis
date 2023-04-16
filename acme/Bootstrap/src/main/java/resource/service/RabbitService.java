package resource.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import resource.broker.RabbitMQConfig;

@Service
public class RabbitService {
    @Autowired
    private ProductService service;

    @Autowired
    private ReviewService reviewService;

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setClassMapper(new DefaultClassMapper());
        converter.setCreateMessageIds(true);
        return converter;
    }

    @RabbitListener(queues = "#{queue_main.name}")
    public void receiveMessageAndDistribute(byte[] messageBytes, @Header("amqp_receivedRoutingKey") String routingKey) {

        System.out.println("#############################################");
        System.out.println("Received Message");
        System.out.println("Doing a: ");
        System.out.println(routingKey);
        System.out.println("#############################################");

        switch (routingKey) {
            case RabbitMQConfig.REVIEW_CREATE_RK, RabbitMQConfig.REVIEW_DELETE_RK, RabbitMQConfig.REVIEW_MODERATE_RK ->
                    reviewService.saveReviewEvent(messageBytes);
            case RabbitMQConfig.PRODUCT_CREATE_RK, RabbitMQConfig.PRODUCT_DELETE_RK -> {
                service.saveProductEvent(messageBytes);
                reviewService.saveReviewEvent(messageBytes);
            }
            case RabbitMQConfig.PRODUCT_UPDATE_RK -> service.saveProductEvent(messageBytes);
            case RabbitMQConfig.BOOTSTRAP_PRODUCT ->
                    service.bootstrap((MessageProperties) SerializationUtils.deserialize(messageBytes));
            case RabbitMQConfig.BOOTSTRAP_REVIEW ->
                    reviewService.bootstrap((MessageProperties) SerializationUtils.deserialize(messageBytes));
        }
    }

}
