package resource.service.command_bus;

import lombok.Getter;
import lombok.Setter;
import resource.model.Review;

import java.util.UUID;

@Getter
@Setter
public class ReviewCreatedForIncompleteVote extends Review {

    private UUID voteID;
    private Long reviewID;

    public ReviewCreatedForIncompleteVote(UUID voteID, Long reviewID) {
        this.voteID = voteID;
        this.reviewID = reviewID;
    }
}
