package resource.service;

import org.springframework.amqp.core.MessageProperties;
import resource.service.command_bus.CreateProductCommand;

public interface ProductService {

    void createProduct(final CreateProductCommand product);

    void deleteProduct(final CreateProductCommand product);

    void updateProduct(final CreateProductCommand product);

    void bootstrap(MessageProperties properties);
}
