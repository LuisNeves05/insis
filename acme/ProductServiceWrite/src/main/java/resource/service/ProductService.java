package resource.service;

import resource.dto.ProductDTO;
import resource.dto.ProductDetailDTO;
import resource.model.Product;

import java.util.Optional;

public interface ProductService {

    ProductDTO create(final Product manager);

    ProductDTO updateBySku(final String sku, final Product product);

    String publishProductMessage(String product);

    void deleteBySku(final String sku);
}
