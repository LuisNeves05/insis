package resource.broker;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "exchange";
    public static final String VOTE_CREATE_RK = "create-vote";
    public static final String VOTE_DELETE_RK = "delete-vote";
    public static final String VOTE_UPDATE_RK = "update-vote";

    @Bean
    Queue queue_main() {
        return new AnonymousQueue();
    }
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }
    @Bean
    Binding binding_create(DirectExchange exchange, Queue queue_main) {
        return BindingBuilder.bind(queue_main).to(exchange).with(VOTE_CREATE_RK);
    }
    @Bean
    Binding binding_delete(DirectExchange exchange, Queue queue_main) {
        return BindingBuilder.bind(queue_main).to(exchange).with(VOTE_DELETE_RK);
    }
    @Bean
    Binding binding_update(DirectExchange exchange, Queue queue_main) {
        return BindingBuilder.bind(queue_main).to(exchange).with(VOTE_UPDATE_RK);
    }
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        return rabbitTemplate;
    }

}
