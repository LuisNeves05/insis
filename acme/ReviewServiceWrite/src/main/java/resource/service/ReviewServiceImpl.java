package resource.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resource.broker.RabbitMQConfig;
import resource.controller.ResourceNotFoundException;
import resource.dto.CreateReviewDTO;
import resource.dto.ReviewDTO;
import resource.dto.VoteReviewDTO;
import resource.model.*;
import resource.repository.ProductRepository;
import resource.repository.ReviewRepository;
import resource.repository.UserRepository;
import resource.service.command_bus.*;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
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


    @Override
    public ReviewDTO create(final CreateReviewDTO createReviewDTO, String sku) {

        final Optional<Product> product = pRepository.findBySku(sku);

        if (product.isEmpty()) return null;

        final var user = uRepository.findByUsername(createReviewDTO.getUsername());

        if (user.isEmpty()) return null;


        Rating rating = null;
        Optional<Rating> r = ratingService.findByRate(createReviewDTO.getRating());
        if (r.isPresent()) {
            rating = r.get();
        }

        LocalDate date = LocalDate.now();

        String funfact = "123";

        Review review = new Review(createReviewDTO.getReviewText(), date, product.orElse(null), funfact, rating, user.orElse(null));

        review = repository.save(review);

        publishReviewMessage(serializeCreateObject(review), RabbitMQConfig.REVIEW_CREATE_RK);

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
                publishReviewMessage(serializeCreateObject(reviewUpdated), RabbitMQConfig.REVIEW_ADD_UP_VOTE_RK);
                return reviewUpdated != null;
            }
        } else if (voteReviewDTO.getVote().equalsIgnoreCase("downVote")) {
            boolean added = review.get().addDownVote(vote);
            if (added) {
                Review reviewUpdated = this.repository.save(review.get());
                publishReviewMessage(serializeCreateObject(reviewUpdated), RabbitMQConfig.REVIEW_ADD_DOWN_VOTE_RK);
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
            publishReviewMessage(serializeDeleteObject(r), RabbitMQConfig.REVIEW_DELETE_RK);
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
        ModerateReviewCommand mrc = new ModerateReviewCommand(review.getIdReview(), review.getApprovalStatus());
        publishReviewMessage(serializeModerateObject(mrc), RabbitMQConfig.REVIEW_MODERATE_RK);
        return ReviewMapper.toDto(review);
    }

    @Override
    public void publishReviewMessage(byte[] payload, String routingKey) {
        this.rabbitMessagingTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                routingKey,
                payload);
    }

    private byte[] serializeCreateObject(Review r) {
        CreateReviewCommand event = new CreateReviewCommand(r.getReviewText(), r.getUser().getUserId(), r.getRating().getRate(), r.getProduct().getSku());
        return SerializationUtils.serialize(event);
    }

    private byte[] serializeDeleteObject(Review r) {
        DeleteReviewCommand event = new DeleteReviewCommand(r.getIdReview());
        return SerializationUtils.serialize(event);
    }

    private byte[] serializeModerateObject(ModerateReviewCommand r) {
        return SerializationUtils.serialize(r);
    }

    @Override
    public void create(final CreateReviewCommand r) {

        final Product product = pRepository.findBySku(r.getProductSku()).orElse(null);

        final var user = uRepository.getById(r.getUserID());

        Rating rating = null;
        Optional<Rating> ra = ratingService.findByRate(r.getRating());
        if (ra.isPresent()) {
            rating = ra.get();
        }

        assert product != null;
        if (repository.findByProductIdAndUserId(product, user).isEmpty()) {

            LocalDate date = LocalDate.now();

            String funfact = "123";

            Review review = new Review(r.getReviewText(), date, product, funfact, rating, user);

            repository.save(review);
        }
    }

    public void moderateReview(ModerateReviewCommand mr) throws ResourceNotFoundException, IllegalArgumentException {

        Review r = repository.findById(mr.getReviewId()).orElse(null);

        assert r != null;
        if (!r.getApprovalStatus().equals(mr.getApproved())) {
            Boolean ap = r.setApprovalStatus(mr.getApproved());

            if (!ap) {
                throw new IllegalArgumentException("Invalid status value");
            }

            repository.save(r);
        }
    }

    @Override
    public void deleteReview(DeleteReviewCommand dr) {

        Optional<Review> rev = repository.findById(dr.reviewId());

        if (rev.isEmpty()) {
            return;
        }
        Review r = rev.get();

        if (r.getUpVote().isEmpty() && r.getDownVote().isEmpty()) {
            repository.delete(r);
        }
    }

    @Override
    public void createReviewForVote(final CreateReviewForVoteCommand r){
        final Product product = pRepository.findBySku(r.getProductSku()).orElse(null);

        final var user = uRepository.getById(r.getUserID());

        assert product != null;
        if (repository.findByProductIdAndUserId(product, user).isEmpty()) {

            LocalDate date = LocalDate.now();

            String funfact = "123";

            Review review = new Review("", date, product, funfact, null, user);

            Review newReview = repository.save(review);

            ReviewCreatedForIncompleteVote crfvc = new ReviewCreatedForIncompleteVote(r.getVoteID(), newReview.getIdReview());

            publishReviewMessage(serializeCreateObject(review), RabbitMQConfig.REVIEW_CREATE_RK);
            publishReviewMessage(serializeCreateObject(crfvc), RabbitMQConfig.CREATE_REVIEW_FOR_INCOMPLETE_VOTE);

        }
    }
}