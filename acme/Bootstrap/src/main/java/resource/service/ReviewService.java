package resource.service;

import org.springframework.amqp.core.MessageProperties;
import resource.service.command_bus.CreateReviewCommand;
import resource.service.command_bus.DeleteReviewCommand;
import resource.service.command_bus.ModerateReviewCommand;

import java.util.List;

public interface ReviewService {

    void create(final CreateReviewCommand r);

    void moderateReview(ModerateReviewCommand mr);

    void deleteReview(DeleteReviewCommand dr);

    void bootstrap(MessageProperties properties);
}
