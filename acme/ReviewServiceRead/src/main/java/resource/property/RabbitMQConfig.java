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
    public static final String REVIEW_EXCHANGE = "review-exchange";
    public static final String REVIEW_QUEUE = "review-queue";
    public static final String REVIEW_ROUTING_KEY = "review-routing-key";

    @Bean
    Queue queue() {
        return new Queue(REVIEW_QUEUE);
    }
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(REVIEW_EXCHANGE);
    }

    @Bean
    Binding binding(DirectExchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(REVIEW_ROUTING_KEY);
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
