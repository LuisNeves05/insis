package resource.service.command_bus;

import lombok.Getter;

@Getter
public record DeleteReviewCommand(Long reviewId) {


}
