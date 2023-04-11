package resource.service;

import resource.dto.ProductDTO;
import resource.model.Product;
import resource.service.command_bus.CreateProductCommand;

public interface ProductService {

    ProductDTO create(final Product manager);

    ProductDTO updateBySku(final String sku, final Product product);

    void publishProductMessage(byte[] payload);

    void deleteBySku(final String sku);
}
