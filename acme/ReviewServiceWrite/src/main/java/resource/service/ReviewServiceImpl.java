package resource.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resource.broker.RabbitMQConfig;
import resource.controller.ResourceNotFoundException;
import resource.dto.CreateReviewDTO;
import resource.dto.ReviewDTO;
import resource.dto.VoteReviewDTO;
import resource.model.*;
import resource.repository.ProductRepository;
import resource.repository.ReviewRepository;
import resource.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository repository;
    @Autowired
    private RabbitTemplate rabbitMessagingTemplate;
    @Autowired
    ProductRepository pRepository;
    @Autowired
    UserRepository uRepository;
    @Autowired
    RatingService ratingService;

    // TODO review everything
    @Override
    public ReviewDTO create(final CreateReviewDTO createReviewDTO, String sku) {

        final Optional<Product> product = pRepository.findBySku(sku);

         if(product.isEmpty()) return null;

        final var user = uRepository.getById(createReviewDTO.getUserID());

        Rating rating = null;
        Optional<Rating> r = ratingService.findByRate(createReviewDTO.getRating());
        if (r.isPresent()) {
            rating = r.get();
        }

        LocalDate date = LocalDate.now();

        String funfact = "123";

        Review review = new Review(createReviewDTO.getReviewText(), date, product.orElse(null), funfact, rating, user);

        review = repository.save(review);

        return ReviewMapper.toDto(review);
    }

    @Override
    public boolean addVoteToReview(Long reviewID, VoteReviewDTO voteReviewDTO) {

        Optional<Review> review = this.repository.findById(reviewID);

        if (review.isEmpty()) return false;

        Vote vote = new Vote(voteReviewDTO.getVote(), voteReviewDTO.getUserID());
        if (voteReviewDTO.getVote().equalsIgnoreCase("upVote")) {
            boolean added = review.get().addUpVote(vote);
            if (added) {
                Review reviewUpdated = this.repository.save(review.get());
                return reviewUpdated != null;
            }
        } else if (voteReviewDTO.getVote().equalsIgnoreCase("downVote")) {
            boolean added = review.get().addDownVote(vote);
            if (added) {
                Review reviewUpdated = this.repository.save(review.get());
                return reviewUpdated != null;
            }
        }
        return false;
    }

    @Override
    public Boolean DeleteReview(Long reviewId) {

        Optional<Review> rev = repository.findById(reviewId);

        if (rev.isEmpty()) {
            return null;
        }
        Review r = rev.get();

        if (r.getUpVote().isEmpty() && r.getDownVote().isEmpty()) {
            repository.delete(r);
            return true;
        }
        return false;
    }

    @Override
    public ReviewDTO moderateReview(Long reviewID, String approved) throws ResourceNotFoundException, IllegalArgumentException {

        Optional<Review> r = repository.findById(reviewID);

        if (r.isEmpty()) {
            throw new ResourceNotFoundException("Review not found");
        }

        Boolean ap = r.get().setApprovalStatus(approved);

        if (!ap) {
            throw new IllegalArgumentException("Invalid status value");
        }

        Review review = repository.save(r.get());

        return ReviewMapper.toDto(review);
    }

    @Override
    public void publishProductMessage(byte[] payload) {
        this.rabbitMessagingTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_EXCHANGE,
                RabbitMQConfig.PRODUCT_ROUTING_KEY,
                payload);
    }
}