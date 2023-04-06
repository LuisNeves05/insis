package resource.service;

import resource.dto.ProductDTO;
import resource.dto.ProductDetailDTO;
import resource.model.Product;

import java.util.Optional;

public interface ProductService {

    Optional<ProductDTO> findBySku(final String sku);

    Optional<Product> getProductBySku(final String sku );

    Iterable<ProductDTO> findByDesignation(final String designation);

    Iterable<ProductDTO> getCatalog();

    ProductDetailDTO getDetails(final String sku);

}
