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
            Product product1 = repository.save(p);
            publishProductMessage(serializeObject(p), RabbitMQConfig.PRODUCT_CREATE_RK);
            return product1.toDto();
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
        publishProductMessage(serializeObject(new CreateProductCommand(productUpdated.getSku(), productUpdated.getDescription(), productUpdated.getDesignation())), RabbitMQConfig.PRODUCT_UPDATE_RK);

        return productUpdated.toDto();
    }

    @Override
    public void deleteBySku(String sku) {

        repository.findBySku(sku).ifPresent(p -> {
            repository.deleteBySku(sku);
            publishProductMessage(serializeObject(p), RabbitMQConfig.PRODUCT_DELETE_RK);});

    }

    @Override
    public void publishProductMessage(byte[] payload, String routingKey) {
        this.rabbitMessagingTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                routingKey,
                payload);
    }

    private byte[] serializeObject(Product p) {
        CreateProductCommand event = new CreateProductCommand(p.getSku(), p.getDescription(), p.getDesignation());
        return SerializationUtils.serialize(event);
    }

    public void create(final CreateProductCommand product) {
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());

        if(repository.findBySku(product.getSku()).orElse(null) == null){
            repository.save(p).toDto();
        }
    }

    public void updateBySku(CreateProductCommand product) {

        final Optional<Product> productToUpdate = repository.findBySku(product.getSku());

        if (productToUpdate.isEmpty()) return;

        productToUpdate.get().updateProduct(new Product(product.getSku(),product.getDesignation(),product.getDescription()));

        repository.save(productToUpdate.get());
    }

    public void deleteBySku(CreateProductCommand p) {

        repository.findBySku(p.getSku()).ifPresent(pr -> repository.deleteBySku(p.getSku()));

    }
}
