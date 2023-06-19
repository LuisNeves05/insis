package resource.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewDTO {

    private String reviewText;

    private String username;

    private Double rating;

    public CreateReviewDTO(){}

    public CreateReviewDTO(String reviewText) {
        this.reviewText = reviewText;
    }

    public CreateReviewDTO(Double rating) {
        this.rating = rating;
    }

}
