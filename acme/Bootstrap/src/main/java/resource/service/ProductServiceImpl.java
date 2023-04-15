package resource.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resource.model.Product;
import resource.repository.ProductRepository;
import resource.service.command_bus.CreateProductCommand;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private RabbitTemplate rabbitMessagingTemplate;


    @Override
    public void createProduct(final CreateProductCommand product) {
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());

        if (repository.findBySku(product.getSku()).orElse(null) == null) {
            repository.save(p);
        }
    }

    @Override
    public void updateProduct(CreateProductCommand product) {

        final Optional<Product> productToUpdate = repository.findBySku(product.getSku());

        if (productToUpdate.isEmpty()) return;

        productToUpdate.get().updateProduct(new Product(product.getSku(), product.getDesignation(), product.getDescription()));

        repository.save(productToUpdate.get());
    }

    @Override
    public void deleteProduct(CreateProductCommand p) {

        repository.findBySku(p.getSku()).ifPresent(pr -> repository.deleteBySku(p.getSku()));

    }

    @Override
    public void bootstrap(MessageProperties properties) {
        Iterable<Product> products = repository.findAll();

        for (Product product : products) {
            CreateProductCommand createProductCommand = new CreateProductCommand(product.getSku(), product.getDescription(), product.getDesignation());
            MessageProperties responseProps = new MessageProperties();
            responseProps.setCorrelationId(properties.getCorrelationId());
            Message response = new Message(SerializationUtils.serialize(createProductCommand), responseProps);
            rabbitMessagingTemplate.convertAndSend("", properties.getReplyTo(), response);
        }
    }
}
