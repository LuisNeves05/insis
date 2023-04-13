package resource.service;

import resource.dto.ProductDTO;
import resource.model.Product;
import resource.service.command_bus.CreateProductCommand;

public interface ProductService {

    ProductDTO create(final Product manager);

    void publishProductMessage(byte[] payload, String routingKey);

    ProductDTO updateBySku(final String sku, final Product product);

    void deleteBySku(final String sku);
}
