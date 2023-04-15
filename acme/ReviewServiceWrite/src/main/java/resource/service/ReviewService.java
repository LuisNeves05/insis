package resource.service;

import resource.dto.CreateReviewDTO;
import resource.dto.ReviewDTO;
import resource.dto.VoteReviewDTO;
import resource.model.Product;
import resource.model.Review;
import resource.service.command_bus.CreateReviewCommand;
import resource.service.command_bus.DeleteReviewCommand;
import resource.service.command_bus.ModerateReviewCommand;

import java.util.List;

public interface ReviewService {


    ReviewDTO create(CreateReviewDTO createReviewDTO, String sku);

    boolean addVoteToReview(Long reviewID, VoteReviewDTO voteReviewDTO);

    Boolean DeleteReview(Long reviewId);

    ReviewDTO moderateReview(Long reviewID, String approved);

    void publishReviewMessage(byte[] payload, String routingKey);

    List<ReviewDTO> findReviewsByUser(Long userID);

    List<ReviewDTO> findPendingReview();

    List<ReviewDTO> getReviewsOfProduct(String sku, String status);

    void deleteReview(DeleteReviewCommand dr);

    void moderateReview(ModerateReviewCommand mr);

    void create(final CreateReviewCommand r);
}
