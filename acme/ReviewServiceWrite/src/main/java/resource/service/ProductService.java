package resource.service;

import resource.service.command_bus.CreateProductCommand;

public interface ProductService {

    void create(final CreateProductCommand product);

    void updateBySku(CreateProductCommand product);

    void deleteBySku(CreateProductCommand p);
}
