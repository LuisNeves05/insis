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
    private String keyEvent;

    public CreateProductCommand(String sku, String description, String designation, String keyEvent) {
        super(sku, designation, description);
        this.sku = sku;
        this.description = description;
        this.designation = designation;
        this.keyEvent = keyEvent;
    }
}