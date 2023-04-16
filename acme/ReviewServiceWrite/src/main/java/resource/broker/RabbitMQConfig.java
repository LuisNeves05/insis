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
    public static final String REVIEW_CREATE_RK = "create-review";
    public static final String REVIEW_DELETE_RK = "delete-review";
    public static final String REVIEW_UPDATE_RK = "update-review";
    public static final String REVIEW_ADD_UP_VOTE_RK = "add-up-vote-review";
    public static final String REVIEW_ADD_DOWN_VOTE_RK = "add-down-vote-review";
    public static final String REVIEW_MODERATE_RK = "moderate-review";
    public static final String PRODUCT_CREATE_RK = "create-product";
    public static final String PRODUCT_DELETE_RK = "delete-product";
    public static final String PRODUCT_UPDATE_RK = "update-product";
    public static final String INCOMPLETE_VOTE = "incomplete-vote-created";
    public static final String CREATE_REVIEW_FOR_INCOMPLETE_VOTE = "create-review-for-incomplete-vote";
    public static final String BOOTSTRAP_REVIEW = "bootstrap-review";


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
        return BindingBuilder.bind(queue_main).to(exchange).with(REVIEW_CREATE_RK);
    }

    @Bean
    Binding binding_delete(DirectExchange exchange, Queue queue_main) {
        return BindingBuilder.bind(queue_main).to(exchange).with(REVIEW_DELETE_RK);
    }

    @Bean
    Binding binding_update(DirectExchange exchange, Queue queue_main) {
        return BindingBuilder.bind(queue_main).to(exchange).with(REVIEW_UPDATE_RK);
    }

    @Bean
    Binding binding_add_up_vote(DirectExchange exchange, Queue queue_main) {
        return BindingBuilder.bind(queue_main).to(exchange).with(REVIEW_ADD_UP_VOTE_RK);
    }

    @Bean
    Binding binding_add_down_vote(DirectExchange exchange, Queue queue_main) {
        return BindingBuilder.bind(queue_main).to(exchange).with(REVIEW_ADD_DOWN_VOTE_RK);
    }

    @Bean
    Binding binding_moderate_review(DirectExchange exchange, Queue queue_main) {
        return BindingBuilder.bind(queue_main).to(exchange).with(REVIEW_MODERATE_RK);
    }

    @Bean
    Binding binding_create_product(DirectExchange exchange, Queue queue_main) {
        return BindingBuilder.bind(queue_main).to(exchange).with(PRODUCT_CREATE_RK);
    }

    @Bean
    Binding binding_delete_product(DirectExchange exchange, Queue queue_main) {
        return BindingBuilder.bind(queue_main).to(exchange).with(PRODUCT_DELETE_RK);
    }

    @Bean
    Binding binding_update_product(DirectExchange exchange, Queue queue_main) {
        return BindingBuilder.bind(queue_main).to(exchange).with(PRODUCT_UPDATE_RK);
    }

    @Bean
    Binding binding_incomplete_vote(DirectExchange exchange, Queue queue_main) {
        return BindingBuilder.bind(queue_main).to(exchange).with(INCOMPLETE_VOTE);
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
