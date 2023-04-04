package resource.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import resource.model.AggregatedRating;
import resource.model.Product;

import java.util.Optional;

public interface AggregatedRatingRepository extends CrudRepository<AggregatedRating, Long> {

    @Query("SELECT a FROM AggregatedRating a WHERE a.product=:product")
    Optional<AggregatedRating> findByProductId(Product product);

}
