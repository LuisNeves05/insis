package resource.service.command_bus;

import lombok.Getter;

import java.io.Serializable;

public record DeleteReviewCommand(Long reviewId) implements Serializable {


}
