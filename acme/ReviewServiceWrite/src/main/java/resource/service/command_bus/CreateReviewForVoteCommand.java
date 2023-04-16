package resource.service.command_bus;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

import java.io.Serializable;

@Getter
@Setter
public class CreateReviewForVoteCommand implements Serializable {
    private UUID voteID;

    private Long userID;

    private String productSku;

    public CreateReviewForVoteCommand(UUID voteID, String productSku, Long userID) {
        this.voteID = voteID;
        this.productSku = productSku;
        this.userID = userID;
    }
}