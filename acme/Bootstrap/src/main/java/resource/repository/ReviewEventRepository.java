package resource.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import resource.model.ReviewEvent;

import java.util.Optional;


public interface ReviewEventRepository extends CrudRepository<ReviewEvent, Long> {
    @Query("SELECT r FROM ReviewEvent r WHERE r.seq=:seq")
    Optional<ReviewEvent> findBySeq(@Param("seq") Long seq);
}
