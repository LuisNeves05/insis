package resource.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import resource.service.command_bus.CreateProductCommand;
import resource.service.command_bus.CreateReviewCommand;
import resource.service.command_bus.DeleteReviewCommand;
import resource.service.command_bus.ModerateReviewCommand;

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
    public void receiveMessageAndDistribute(byte[] messageBytes, @Header("amqp_receivedRoutingKey") String routingKey) throws JsonProcessingException {

        System.out.println("#############################################");
        System.out.println("Received Message");
        System.out.println("Doing a: ");
        System.out.println(routingKey);
        System.out.println("#############################################");

        switch (routingKey) {
            case RabbitMQConfig.REVIEW_CREATE_RK -> {
                CreateReviewCommand event = (CreateReviewCommand) SerializationUtils.deserialize(messageBytes);
                reviewService.create(event);
            }
            case RabbitMQConfig.REVIEW_DELETE_RK -> {
                DeleteReviewCommand deleteReviewCommand =
                        (DeleteReviewCommand) SerializationUtils.deserialize(messageBytes);
                reviewService.deleteReview(deleteReviewCommand);
            }
            // TODO VER ISTO DOS VOTOS E COMO VAI FICAR
            /*case RabbitMQConfig.REVIEW_ADD_DOWN_VOTE_RK -> service.addVoteToReview();
            case RabbitMQConfig.REVIEW_ADD_UP_VOTE_RK -> service.addVoteToReview(); */
            case RabbitMQConfig.REVIEW_MODERATE_RK -> {
                ModerateReviewCommand moderateReviewCommand =
                        (ModerateReviewCommand) SerializationUtils.deserialize(messageBytes);
                reviewService.moderateReview(moderateReviewCommand);
            }
            case RabbitMQConfig.PRODUCT_CREATE_RK -> {
                CreateProductCommand event = (CreateProductCommand) SerializationUtils.deserialize(messageBytes);
                service.createProduct(event);
            }
            case RabbitMQConfig.PRODUCT_DELETE_RK -> {
                CreateProductCommand event = (CreateProductCommand) SerializationUtils.deserialize(messageBytes);
                service.deleteProduct(event);
            }
            case RabbitMQConfig.PRODUCT_UPDATE_RK -> {
                CreateProductCommand event = (CreateProductCommand) SerializationUtils.deserialize(messageBytes);
                service.updateProduct(event);
            }
            case RabbitMQConfig.BOOTSTRAP_PRODUCT ->
                    service.bootstrap((MessageProperties) SerializationUtils.deserialize(messageBytes));
            case RabbitMQConfig.BOOTSTRAP_REVIEW ->
                    reviewService.bootstrap((MessageProperties) SerializationUtils.deserialize(messageBytes));
        }
    }

}