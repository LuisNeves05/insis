package resource.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import resource.broker.RabbitMQConfig;
import resource.dto.ProductDTO;
import resource.model.Product;
import resource.repository.ProductRepository;
import org.springframework.messaging.handler.annotation.Header;
import resource.repository.ReviewRepository;
import resource.service.command_bus.CreateReviewCommand;
import resource.service.command_bus.DeleteReviewCommand;
import resource.service.command_bus.ModerateReviewCommand;

import java.util.Optional;

@Service
public class ReviewServiceRabbit {

    @Autowired
    private ReviewServiceImpl service;

    @Autowired
    private ReviewRepository repository;

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
            case RabbitMQConfig.REVIEW_CREATE_RK -> {
                CreateReviewCommand event = (CreateReviewCommand) SerializationUtils.deserialize(messageBytes);
                service.create(event);
            }
            case RabbitMQConfig.REVIEW_DELETE_RK -> {
                DeleteReviewCommand deleteReviewCommand =
                        (DeleteReviewCommand) SerializationUtils.deserialize(messageBytes);
                service.deleteReview(deleteReviewCommand);
            }
            // TODO VER ISTO DOS VOTOS E COMO VAI FICAR
            /*case RabbitMQConfig.REVIEW_ADD_DOWN_VOTE_RK -> service.addVoteToReview();
            case RabbitMQConfig.REVIEW_ADD_UP_VOTE_RK -> service.addVoteToReview(); */
            case RabbitMQConfig.REVIEW_MODERATE_RK -> {
                ModerateReviewCommand moderateReviewCommand =
                        (ModerateReviewCommand) SerializationUtils.deserialize(messageBytes);
                service.moderateReview(moderateReviewCommand);
            }
        }
    }


}
