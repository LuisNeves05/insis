package resource.service.command_bus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModerateReviewCommand {

    private Long reviewId;
    private String approved;

    public ModerateReviewCommand(Long reviewId, String approved) {
        this.reviewId = reviewId;
        this.approved = approved;
    }
}
