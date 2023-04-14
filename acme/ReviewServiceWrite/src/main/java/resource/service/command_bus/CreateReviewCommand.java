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
    private String productSku;

    public CreateReviewCommand(String reviewText, Long userID, Double rating, String productSku) {
        this.reviewText = reviewText;
        this.userID = userID;
        this.rating = rating;
        this.productSku = productSku;
    }
}