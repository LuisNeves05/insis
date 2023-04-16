package resource.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resource.model.ProductEvent;
import resource.repository.ProductEventRepository;
import resource.service.command_bus.CreateProductCommand;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductEventRepository repository;

    @Autowired
    private RabbitTemplate rabbitMessagingTemplate;


    @Override
    public void saveProductEvent(byte[] event) {
        List<ProductEvent> productEventList = (List<ProductEvent>) repository.findAll();
        repository.save(new ProductEvent(event, (long) (productEventList.size() + 1)));
    }

    @Override
    public void bootstrap(MessageProperties properties) {
        ProductEvent event = repository.findBySeq(Long.parseLong(properties.getHeader("id").toString())).orElse(null);
        MessageProperties responseProps = new MessageProperties();
        responseProps.setCorrelationId(properties.getCorrelationId());
        Message response;
        if(event != null) {
            CreateProductCommand createProductCommand = (CreateProductCommand) SerializationUtils.deserialize(event.getEvent());
            response = new Message(SerializationUtils.serialize(createProductCommand), responseProps);
        }else {
            response = new Message(SerializationUtils.serialize(new byte[0]), responseProps);
        }
        rabbitMessagingTemplate.convertAndSend("", properties.getReplyTo(), response);
    }

}
