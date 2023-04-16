package resource.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resource.broker.RabbitMQConfig;
import resource.model.ProductEvent;
import resource.model.ReviewEvent;
import resource.repository.ReviewEventRepository;
import resource.service.command_bus.CreateProductCommand;
import resource.service.command_bus.CreateReviewCommand;
import resource.service.command_bus.DeleteReviewCommand;
import resource.service.command_bus.ModerateReviewCommand;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewEventRepository repository;
    @Autowired
    private RabbitTemplate rabbitMessagingTemplate;

    @Override
    public void saveReviewEvent(byte[] event) {
        List<ReviewEvent> productEventList = (List<ReviewEvent>) repository.findAll();
        repository.save(new ReviewEvent(event, (long) (productEventList.size() + 1)));
    }

    @Override
    public void bootstrap(MessageProperties properties) {
        ReviewEvent event = repository.findBySeq(Long.parseLong(properties.getHeader("id").toString())).orElse(null);
        MessageProperties responseProps = new MessageProperties();
        responseProps.setCorrelationId(properties.getCorrelationId());
        Message response = null;
        if (event != null) {
            Object command = SerializationUtils.deserialize(event.getEvent());
            if (command instanceof CreateProductCommand) {
                response = new Message(SerializationUtils.serialize(command), responseProps);
            } else if (command instanceof CreateReviewCommand) {
                response = new Message(SerializationUtils.serialize(command), responseProps);
            } else if (command instanceof DeleteReviewCommand) {
                response = new Message(SerializationUtils.serialize(command), responseProps);
            } else if (command instanceof ModerateReviewCommand) {
                response = new Message(SerializationUtils.serialize(command), responseProps);
            } else {
                // TODO
            }
        } else {
            response = new Message(SerializationUtils.serialize(new byte[0]), responseProps);
        }

        rabbitMessagingTemplate.convertAndSend("", properties.getReplyTo(), response);
    }
}