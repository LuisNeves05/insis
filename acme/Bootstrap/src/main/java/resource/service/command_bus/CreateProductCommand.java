package resource.service.command_bus;

import lombok.Getter;
import lombok.Setter;
import resource.model.Product;

import java.io.Serializable;

@Getter
@Setter
public class CreateProductCommand extends Product implements Serializable {
    private String sku;
    private String designation;
    private String description;

    public CreateProductCommand(String sku, String description, String designation) {
        super(sku, designation, description);
        this.sku = sku;
        this.description = description;
        this.designation = designation;
    }
}