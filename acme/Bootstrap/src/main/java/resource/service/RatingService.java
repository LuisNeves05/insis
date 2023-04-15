package resource.service;

import org.springframework.amqp.core.MessageProperties;
import resource.model.Rating;

import java.util.Optional;

public interface RatingService {

    Optional<Rating> findByRate(Double rate);

    void bootstrap(MessageProperties properties);
}
