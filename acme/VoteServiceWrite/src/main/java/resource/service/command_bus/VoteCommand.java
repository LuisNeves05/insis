package resource.service.command_bus;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class VoteCommand implements Serializable {
    private Long reviewId;
    private String username;
    private String status;

    public VoteCommand(Long reviewId, String username, String status) {
        this.reviewId = reviewId;
        this.username = username;
        this.status = status;
    }
}