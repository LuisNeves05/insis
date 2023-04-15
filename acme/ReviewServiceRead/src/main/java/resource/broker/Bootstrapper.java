package resource.broker;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import resource.service.ProductService;
import resource.service.command_bus.CreateProductCommand;

import java.io.IOException;
import java.util.UUID;


@Component
public class Bootstrapper {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private ProductService service;

    @Autowired
    public Bootstrapper(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws IOException, InterruptedException {
        requestAllProducts();
    }

    public void requestAllProducts() throws IOException, InterruptedException {
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.getConnectionFactory().createConnection().createChannel(false).queueDeclare().getQueue();

        MessageProperties properties = new MessageProperties();
        properties.setCorrelationId(correlationId);
        properties.setReplyTo(replyQueueName);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.BOOTSTRAP_PRODUCT, SerializationUtils.serialize(properties));

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
        container.setQueueNames(replyQueueName);
        container.setMessageListener(new MessageListenerAdapter(this, "handleResponse"));

        container.start();
        Thread.sleep(10000L); // wait for a response for 10 seconds
        container.stop();
    }

    public void handleResponse(byte[] messageBytes) {
        CreateProductCommand createProductCommand = (CreateProductCommand) SerializationUtils.deserialize(messageBytes);
        service.create(createProductCommand);
    }
}

