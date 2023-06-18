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

    Iterable<Review> getAll();

    List<ReviewDTO> getReviewsOfProduct(String sku, String status);

    ReviewDTO create(CreateReviewDTO createReviewDTO, String sku);

    boolean addVoteToReview(Long reviewID, VoteReviewDTO voteReviewDTO);

    Double getWeightedAverage(Product product);

    Boolean DeleteReview(Long reviewId);

    List<ReviewDTO> findPendingReview();

    List<ReviewDTO> findAllReviews();

    ReviewDTO moderateReview(Long reviewID, String approved);

    List<ReviewDTO> findReviewsByUser(Long userID);

    void deleteReview(DeleteReviewCommand dr);

    void moderateReview(ModerateReviewCommand mr);

    void create(final CreateReviewCommand r);
}
