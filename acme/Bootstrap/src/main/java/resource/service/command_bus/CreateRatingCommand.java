package resource.service.command_bus;

import lombok.Getter;
import lombok.Setter;
import resource.model.Rating;

@Getter
@Setter
public class CreateRatingCommand extends Rating {

    private Double rate;

    public CreateRatingCommand(Double rate) {
        super(rate);
        this.rate = rate;
    }
}
