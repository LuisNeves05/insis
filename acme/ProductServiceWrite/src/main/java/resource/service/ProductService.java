package resource.service;

import resource.dto.ProductDTO;
import resource.model.Product;
import resource.service.command_bus.CreateProductCommand;

public interface ProductService {

    ProductDTO create(final Product manager);

    void publishProductMessage(byte[] payload, String routingKey);

    ProductDTO updateBySku(final String sku, final Product product);

    void deleteBySku(final String sku);

    void create(final CreateProductCommand product);

    void updateBySku(CreateProductCommand product);

    void deleteBySku(CreateProductCommand p);
}
