package resource.service;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Service;
import resource.ProductServiceWriteApplication;
import resource.dto.ProductDTO;
import resource.model.Product;
import resource.property.RabbitMQConfig;
import resource.repository.ProductRepository;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;
    @Autowired
    private MappingJackson2MessageConverter mappingJackson2MessageConverter;

    @Override
    public String publishProductMessage(String product) {
        this.rabbitMessagingTemplate.setMessageConverter(this.mappingJackson2MessageConverter);
        this.rabbitMessagingTemplate.convertAndSend(RabbitMQConfig.PRODUCT_EXCHANGE, RabbitMQConfig.PRODUCT_ROUTING_KEY,product);
        return "Product Message Published";
    }
    @Autowired
    private ProductRepository repository;


    @Override
    public ProductDTO create(final Product product) {
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());

        return repository.save(p).toDto();
    }

    @Override
    public ProductDTO updateBySku(String sku, Product product) {
        
        final Optional<Product> productToUpdate = repository.findBySku(sku);

        if( productToUpdate.isEmpty() ) return null;

        productToUpdate.get().updateProduct(product);

        Product productUpdated = repository.save(productToUpdate.get());
        
        return productUpdated.toDto();
    }

    @Override
    public void deleteBySku(String sku) {
        repository.deleteBySku(sku);
    }
}
