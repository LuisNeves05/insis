package resource.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDTO {

    private String username;

    private Long reviewId;

    private String status;


}
