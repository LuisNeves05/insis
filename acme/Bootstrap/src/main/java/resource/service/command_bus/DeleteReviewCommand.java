package resource.service.command_bus;


import java.io.Serializable;

public record DeleteReviewCommand(Long reviewId) implements Serializable {


}
