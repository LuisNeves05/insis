package resource.model;

import resource.dto.ReviewDTO;

import java.util.ArrayList;
import java.util.List;

public class ReviewMapper {

    public static ReviewDTO toDto(Review review){
        return new ReviewDTO(review.getIdReview(), review.getReviewText(), review.getPublishingDate(), review.getApprovalStatus(), review.getFunFact(), review.getRating().getRate(), review.getUpVote().size(), review.getUser().getUserId(),review.getProduct().getSku());
    }

    public static List<ReviewDTO> toDtoList(List<Review> review) {
        List<ReviewDTO> dtoList = new ArrayList<>();

        for (Review rev: review) {
            dtoList.add(toDto(rev));
        }
        return dtoList;
    }
}
