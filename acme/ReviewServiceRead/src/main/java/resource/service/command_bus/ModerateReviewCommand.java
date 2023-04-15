package resource.service.command_bus;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ModerateReviewCommand implements Serializable {

    private Long reviewId;
    private String approved;

    public ModerateReviewCommand(Long reviewId, String approved) {
        this.reviewId = reviewId;
        this.approved = approved;
    }
}
