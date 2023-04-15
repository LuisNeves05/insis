package resource.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Service
public class ProductServiceRabbit {
    @Autowired
    private ProductService service;

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
        CreateProductCommand event = (CreateProductCommand) SerializationUtils.deserialize(messageBytes);

        switch (routingKey) {
            case RabbitMQConfig.PRODUCT_CREATE_RK -> service.create(event);
            case RabbitMQConfig.PRODUCT_DELETE_RK -> service.deleteBySku(event);
            case RabbitMQConfig.PRODUCT_UPDATE_RK -> service.updateBySku(event);
        }
    }

}