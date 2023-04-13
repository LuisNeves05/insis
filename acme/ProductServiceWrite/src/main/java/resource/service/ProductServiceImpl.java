package resource.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resource.dto.ProductDTO;
import resource.model.Product;
import resource.property.RabbitMQConfig;
import resource.repository.ProductRepository;
import resource.service.command_bus.CreateProductCommand;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private RabbitTemplate rabbitMessagingTemplate;

    @Autowired
    private ProductRepository repository;

    @Override
    public ProductDTO create(final Product product) {
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());

        // Send the message to the exchange with the routing key
        publishProductMessage(serializeObject(p), RabbitMQConfig.PRODUCT_CREATE_RK);

        return repository.save(p).toDto();
    }


    @Override
    public ProductDTO updateBySku(String sku, Product product) {

        final Optional<Product> productToUpdate = repository.findBySku(sku);

        if (productToUpdate.isEmpty()) return null;

        productToUpdate.get().updateProduct(product);

        Product productUpdated = repository.save(productToUpdate.get());

        // Send the message to the exchange with the routing key
        publishProductMessage(serializeObject(productUpdated), RabbitMQConfig.PRODUCT_UPDATE_RK);

        return productUpdated.toDto();
    }

    @Override
    public void deleteBySku(String sku) {

        repository.findBySku(sku).ifPresent(p ->
                publishProductMessage(serializeObject(p), RabbitMQConfig.PRODUCT_DELETE_RK));

        repository.deleteBySku(sku);
    }

    @Override
    public void publishProductMessage(byte[] payload, String routingKey) {
        this.rabbitMessagingTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_EXCHANGE,
                routingKey,
                payload);
    }

    private byte[] serializeObject(Product p) {
        CreateProductCommand event = new CreateProductCommand(p.getSku(), p.getDescription(), p.getDesignation());
        return SerializationUtils.serialize(event);
    }

    public void create(final CreateProductCommand product) {
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());

        if (repository.findBySku(product.getSku()).isPresent()) {
            repository.save(p).toDto();
        }
    }

    public void updateBySku(CreateProductCommand product) {

        final Optional<Product> productToUpdate = repository.findBySku(product.getSku());

        if (productToUpdate.isEmpty()) return;

        productToUpdate.get().updateProduct(product);

        repository.save(productToUpdate.get());
    }

    public void deleteBySku(CreateProductCommand p) {

        repository.findBySku(p.getSku()).ifPresent(pr -> repository.deleteBySku(p.getSku()));

    }
}
