package resource.service;

import org.springframework.amqp.core.MessageProperties;

public interface ProductService {

    void saveProductEvent(byte[] event);

    void bootstrap(MessageProperties properties);
}
