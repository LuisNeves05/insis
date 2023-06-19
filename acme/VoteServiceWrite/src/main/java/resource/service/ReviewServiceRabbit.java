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
import org.springframework.messaging.handler.annotation.Header;
import resource.service.command_bus.*;

@Service
public class ReviewServiceRabbit {

    @Autowired
    private VoteService service;

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
            case RabbitMQConfig.VOTE_CREATE_RK -> {
                VoteCommand event = (VoteCommand) SerializationUtils.deserialize(messageBytes);
                service.create(event);
            }
            case RabbitMQConfig.VOTE_DELETE_RK -> {
                DeleteVoteCommand deleteReviewCommand =
                        (DeleteVoteCommand) SerializationUtils.deserialize(messageBytes);
                service.deleteReview(deleteReviewCommand);
            }
        }
    }

}
