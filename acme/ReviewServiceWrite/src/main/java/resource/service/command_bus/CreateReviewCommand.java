package resource.service.command_bus;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateReviewCommand implements Serializable {
    private String reviewText;
    private Long userID;
    private Double rating;

    public CreateReviewCommand(String reviewText, Long userID, Double rating) {
        this.reviewText = reviewText;
        this.userID = userID;
        this.rating = rating;
    }
}