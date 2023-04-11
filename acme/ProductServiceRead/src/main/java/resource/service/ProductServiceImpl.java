package resource.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Delivery;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import resource.dto.ProductDTO;
import resource.dto.ProductDetailDTO;
import resource.model.Product;
import resource.repository.ProductRepository;
import resource.service.command_bus.CreateProductCommand;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    public static final String PRODUCT_QUEUE = "product-queue";
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Optional<Product> getProductBySku(final String sku) {

        return repository.findBySku(sku);
    }

    @Override
    public Optional<ProductDTO> findBySku(String sku) {
        final Optional<Product> product = repository.findBySku(sku);

        if (product.isEmpty())
            return Optional.empty();
        else
            return Optional.of(product.get().toDto());
    }


    @Override
    public Iterable<ProductDTO> findByDesignation(final String designation) {
        Iterable<Product> p = repository.findByDesignation(designation);
        List<ProductDTO> pDto = new ArrayList();
        for (Product pd : p) {
            pDto.add(pd.toDto());
        }

        return pDto;
    }

    @Override
    public Iterable<ProductDTO> getCatalog() {
        Iterable<Product> p = repository.findAll();
        List<ProductDTO> pDto = new ArrayList();
        for (Product pd : p) {
            pDto.add(pd.toDto());
        }

        return pDto;
    }

    public ProductDetailDTO getDetails(String sku) {

        Optional<Product> p = repository.findBySku(sku);

        if (p.isEmpty())
            return null;
        else
            return new ProductDetailDTO(p.get().getSku(), p.get().getDesignation(), p.get().getDescription());
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setClassMapper(new DefaultClassMapper());
        converter.setCreateMessageIds(true);
        return converter;
    }


    @RabbitListener(queues = PRODUCT_QUEUE)
    public void receiveMessageAndCreateProduct(Delivery delivery) {
        try {
            System.out.println("####################################");
            byte[] objectBytes = delivery.getBody();
            ByteArrayInputStream bis = new ByteArrayInputStream(objectBytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            CreateProductCommand myObject = (CreateProductCommand) ois.readObject();
            System.out.println("#############################################################################");
            System.out.println("#############################################################################");
            System.out.println("#############################################################################");
            System.out.println(myObject);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }



        // update the read database
        //repository.save(event);
    }
}
