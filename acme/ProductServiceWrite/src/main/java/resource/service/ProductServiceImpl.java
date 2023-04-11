package resource.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Service;
import resource.dto.ProductDTO;
import resource.model.Product;
import resource.property.RabbitMQConfig;
import resource.repository.ProductRepository;
import resource.service.command_bus.CreateProductCommand;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;
    @Autowired
    private MappingJackson2MessageConverter mappingJackson2MessageConverter;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void publishProductMessage(byte[] payload) {
        this.rabbitMessagingTemplate.setMessageConverter(this.mappingJackson2MessageConverter);
        this.rabbitMessagingTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_EXCHANGE,
                RabbitMQConfig.PRODUCT_ROUTING_KEY,
                payload);
    }

    @Autowired
    private ProductRepository repository;


    @Override
    public ProductDTO create(final Product product) {
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());
        CreateProductCommand event = new CreateProductCommand(p.getSku(), p.getDescription(), p.getDesignation());
        // byte[] payload = objectMapper.writeValueAsBytes(event);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(event);
            byte[] objectBytes = bos.toByteArray();
            publishProductMessage(objectBytes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return repository.save(p).toDto();
    }

    @Override
    public ProductDTO updateBySku(String sku, Product product) {

        final Optional<Product> productToUpdate = repository.findBySku(sku);

        if (productToUpdate.isEmpty()) return null;

        productToUpdate.get().updateProduct(product);

        Product productUpdated = repository.save(productToUpdate.get());

        return productUpdated.toDto();
    }

    @Override
    public void deleteBySku(String sku) {
        repository.deleteBySku(sku);
    }
}
