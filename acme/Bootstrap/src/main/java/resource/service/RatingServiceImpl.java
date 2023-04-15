package resource.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resource.model.Rating;
import resource.model.Review;
import resource.repository.RatingRepository;
import resource.service.command_bus.CreateRatingCommand;
import resource.service.command_bus.CreateReviewCommand;

import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService{

    @Autowired
    RatingRepository repository;

    @Autowired
    private RabbitTemplate rabbitMessagingTemplate;

    public Optional<Rating> findByRate(Double rate){
        return repository.findByRate(rate);
    }

    @Override
    public void bootstrap(MessageProperties properties) {
        Iterable<Rating> ratings = repository.findAll();

        for (Rating rating : ratings) {
            CreateRatingCommand createRatingCommand = new CreateRatingCommand(rating.getRate());
            MessageProperties responseProps = new MessageProperties();
            responseProps.setCorrelationId(properties.getCorrelationId());
            Message response = new Message(SerializationUtils.serialize(createRatingCommand), responseProps);
            rabbitMessagingTemplate.convertAndSend("", properties.getReplyTo(), response);
        }
    }

}
