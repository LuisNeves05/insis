package resource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resource.dto.ProductDTO;
import resource.dto.ProductDetailDTO;
import resource.model.Product;
import resource.repository.ProductRepository;
import resource.service.command_bus.CreateProductCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repository;

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
        List<ProductDTO> pDto = new ArrayList<>();
        for (Product pd : p) {
            pDto.add(pd.toDto());
        }

        return pDto;
    }

    @Override
    public Iterable<ProductDTO> getCatalog() {
        Iterable<Product> p = repository.findAll();
        List<ProductDTO> pDto = new ArrayList<>();
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

    public void create(final CreateProductCommand product) {
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());

        if (repository.findBySku(product.getSku()).isPresent()) {
            repository.save(p).toDto();
        }
    }

    public void updateBySku(CreateProductCommand product) {

        final Optional<Product> productToUpdate = repository.findBySku(product.getSku());

        if (productToUpdate.isEmpty()) return;

        productToUpdate.get().updateProduct(product);

        repository.save(productToUpdate.get());
    }

    public void deleteBySku(CreateProductCommand p) {

        repository.findBySku(p.getSku()).ifPresent(pr -> repository.deleteBySku(p.getSku()));

    }

}
