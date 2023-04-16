package resource.service;

import org.springframework.amqp.core.MessageProperties;

public interface ReviewService {

    void saveReviewEvent(byte[] event);

    void bootstrap(MessageProperties properties);
}
