package resource.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resource.dto.ProductDTO;
import resource.model.Product;
import resource.broker.RabbitMQConfig;
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
        if(repository.findBySku(product.getSku()).orElse(null) == null) {
            Product productUpdated = repository.save(p);
            publishProductMessage(serializeObject(new CreateProductCommand(productUpdated.getSku(), productUpdated.getDescription(), productUpdated.getDesignation(), RabbitMQConfig.PRODUCT_CREATE_RK)), RabbitMQConfig.PRODUCT_CREATE_RK);
            return productUpdated.toDto();
        }
        return null;
    }


    @Override
    public ProductDTO updateBySku(String sku, Product product) {

        final Optional<Product> productToUpdate = repository.findBySku(sku);

        if (productToUpdate.isEmpty()) return null;

        productToUpdate.get().updateProduct(product);

        Product productUpdated = repository.save(productToUpdate.get());

        // Send the message to the exchange with the routing key
        publishProductMessage(serializeObject(new CreateProductCommand(productUpdated.getSku(), productUpdated.getDescription(), productUpdated.getDesignation(), RabbitMQConfig.PRODUCT_UPDATE_RK)), RabbitMQConfig.PRODUCT_UPDATE_RK);

        return productUpdated.toDto();
    }

    @Override
    public void deleteBySku(String sku) {

        repository.findBySku(sku).ifPresent(p -> {
            repository.deleteBySku(sku);
            publishProductMessage(serializeObject(new CreateProductCommand(p.getSku(), p.getDescription(), p.getDesignation(), RabbitMQConfig.PRODUCT_DELETE_RK)), RabbitMQConfig.PRODUCT_DELETE_RK);});

    }

    @Override
    public void publishProductMessage(byte[] payload, String routingKey) {
        this.rabbitMessagingTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                routingKey,
                payload);

    }

    private byte[] serializeObject(CreateProductCommand p) {
        return SerializationUtils.serialize(p);
    }

    @Override
    public void create(final CreateProductCommand product) {
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());

        if (repository.findBySku(product.getSku()).orElse(null) == null) {
            repository.save(p).toDto();
        }
    }
    @Override
    public void updateBySku(CreateProductCommand product) {

        final Optional<Product> productToUpdate = repository.findBySku(product.getSku());

        if (productToUpdate.isEmpty()) return;

        productToUpdate.get().updateProduct(new Product(product.getSku(), product.getDesignation(), product.getDescription()));

        repository.save(productToUpdate.get());
    }

    @Override
    public void deleteBySku(CreateProductCommand p) {

        repository.findBySku(p.getSku()).ifPresent(pr -> repository.deleteBySku(p.getSku()));

    }
}
