package resource.service;

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

    @Override
    public void create(final CreateProductCommand product) {
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());

        if (repository.findBySku(product.getSku()).orElse(null) == null) {
            repository.save(p).toDto();
        }
    }
    @Override
    public void updateBySku(CreateProductCommand product) {

        final Optional<Product> productToUpdate = repository.findBySku(product.getSku());

        if (productToUpdate.isEmpty()) return;

        productToUpdate.get().updateProduct(new Product(product.getSku(), product.getDesignation(), product.getDescription()));

        repository.save(productToUpdate.get());
    }

    @Override
    public void deleteBySku(CreateProductCommand p) {

        repository.findBySku(p.getSku()).ifPresent(pr -> repository.deleteBySku(p.getSku()));

    }
}
