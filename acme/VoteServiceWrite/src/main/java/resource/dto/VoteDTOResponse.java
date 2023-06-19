package resource.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDTOResponse {

    private Long voteId;
    private String username;
    private Long reviewId;
    private String status;

    public VoteDTOResponse(Long voteId, String username, Long reviewId, String status) {
        this.voteId = voteId;
        this.username = username;
        this.reviewId = reviewId;
        this.status = status;
    }
}
