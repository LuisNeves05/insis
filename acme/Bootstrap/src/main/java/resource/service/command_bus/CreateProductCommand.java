package resource.service.command_bus;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateProductCommand implements Serializable {
    private String sku;
    private String designation;
    private String description;
    private String keyEvent;

    public CreateProductCommand(String sku, String description, String designation, String keyEvent) {
        this.sku = sku;
        this.description = description;
        this.designation = designation;
        this.keyEvent = keyEvent;
    }
}