package resource.property;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class RabbitMQConfig {
    public static final String PRODUCT_EXCHANGE = "product-exchange";
    public static final String PRODUCT_QUEUE = "product-queue";
    public static final String PRODUCT_ROUTING_KEY = "product-routing-key";

    @Bean
    Queue queue() {
        return new Queue(PRODUCT_QUEUE);
    }
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    Binding binding(DirectExchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(PRODUCT_ROUTING_KEY);
    }

    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(jackson2Converter());
        return factory;
    }
}
