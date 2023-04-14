package resource.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import resource.model.Rating;

import java.util.Optional;

public interface RatingRepository extends CrudRepository<Rating, Long> {

    @Query("SELECT r FROM Rating r WHERE r.rate=:rate")
    Optional<Rating> findByRate(@Param("rate")Double rate);

}
