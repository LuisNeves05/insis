package resource.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resource.model.Product;
import resource.model.Rating;
import resource.model.Review;
import resource.repository.ProductRepository;
import resource.repository.ReviewRepository;
import resource.repository.UserRepository;
import resource.service.command_bus.CreateProductCommand;
import resource.service.command_bus.CreateReviewCommand;
import resource.service.command_bus.DeleteReviewCommand;
import resource.service.command_bus.ModerateReviewCommand;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository repository;
    @Autowired
    ProductRepository pRepository;
    @Autowired
    UserRepository uRepository;
    @Autowired
    RatingService ratingService;

    @Autowired
    private RabbitTemplate rabbitMessagingTemplate;


    @Override
    public void create(final CreateReviewCommand r) {

        final Optional<Product> product = pRepository.findBySku(r.getProductSku());

        if(product.isEmpty()) return;

        final var user = uRepository.getById(r.getUserID());

        Rating rating = null;
        Optional<Rating> ra = ratingService.findByRate(r.getRating());
        if (ra.isPresent()) {
            rating = ra.get();
        }

        LocalDate date = LocalDate.now();

        String funfact = "123";

        Review review = new Review(r.getReviewText(), date, product.orElse(null), funfact, rating, user);

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

    @Override
    public void bootstrap(MessageProperties properties) {
        Iterable<Review> reviews = repository.findAll();

        for (Review review : reviews) {
            CreateReviewCommand createReviewCommand = new CreateReviewCommand(review.getReviewText(), review.getUser().getUserId(), review.getRating().getRate(), review.getProduct().getSku());
            MessageProperties responseProps = new MessageProperties();
            responseProps.setCorrelationId(properties.getCorrelationId());
            Message response = new Message(SerializationUtils.serialize(createReviewCommand), responseProps);
            rabbitMessagingTemplate.convertAndSend("", properties.getReplyTo(), response);
        }
    }
}