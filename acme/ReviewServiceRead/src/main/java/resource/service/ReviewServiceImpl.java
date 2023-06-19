package resource.service;

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
import resource.service.command_bus.CreateProductCommand;
import resource.service.command_bus.CreateReviewCommand;
import resource.service.command_bus.DeleteReviewCommand;
import resource.service.command_bus.ModerateReviewCommand;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static resource.model.ReviewMapper.toDtoList;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository repository;
    @Autowired
    ProductRepository pRepository;
    @Autowired
    UserRepository uRepository;
    @Autowired
    RatingService ratingService;

    @Override
    public Iterable<Review> getAll() {
        return repository.findAll();
    }

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

        String funfact = "123123";

        Review review = new Review(createReviewDTO.getReviewText(), date, product.orElse(null), funfact, rating, user.orElse(null));

        review = repository.save(review);

        return ReviewMapper.toDto(review);
    }

    @Override
    public List<ReviewDTO> getReviewsOfProduct(String sku, String status) {

        // Optional<Product> product = pRepository.findBySku(sku);
        // if( product.isEmpty() ) return null;

        // TODO remove null
        Optional<List<Review>> r = repository.findByProductIdStatus(null, status);

        return r.map(ReviewMapper::toDtoList).orElse(null);

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
    public Double getWeightedAverage(Product product) {

        Optional<List<Review>> r = repository.findByProductId(product);

        if (r.isEmpty()) return 0.0;

        double sum = 0;

        for (Review rev : r.get()) {
            Rating rate = rev.getRating();

            if (rate != null) {
                sum += rate.getRate();
            }
        }

        return sum / r.get().size();
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
    public List<ReviewDTO> findPendingReview() {

        Optional<List<Review>> r = repository.findPendingReviews();

        return r.map(ReviewMapper::toDtoList).orElse(null);

    }
    @Override
    public List<ReviewDTO> findAllReviews() {
        List<Review> r = (List<Review>) repository.findAll();

        return toDtoList(r);
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
    public List<ReviewDTO> findReviewsByUser(Long userID) {

        final Optional<UserR> user = uRepository.findById(userID);

        if(user.isEmpty()) return null;

        Optional<List<Review>> r = repository.findByUserId(user.orElse(null));

        return r.map(ReviewMapper::toDtoList).orElse(null);

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

        LocalDate date = LocalDate.now();

        String funfact = "123";

        Review review = new Review(r.getReviewText(), date, product, funfact, rating, user);

        repository.save(review);
    }
    @Override
    public void moderateReview(ModerateReviewCommand mr) throws ResourceNotFoundException, IllegalArgumentException {

        Optional<Review> r = repository.findById(mr.getReviewId());

        if (r.isEmpty()) {
            throw new ResourceNotFoundException("Review not found");
        }

        Boolean ap = r.get().setApprovalStatus(mr.getApproved());

        if (!ap) {
            throw new IllegalArgumentException("Invalid status value");
        }

        repository.save(r.get());
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
}