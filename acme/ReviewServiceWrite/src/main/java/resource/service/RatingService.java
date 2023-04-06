package resource.service;

import resource.model.Rating;

import java.util.Optional;

public interface RatingService {

    Optional<Rating> findByRate(Double rate);
}
