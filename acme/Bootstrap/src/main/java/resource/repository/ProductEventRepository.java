package resource.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import resource.model.ProductEvent;

import java.util.Optional;

public interface ProductEventRepository extends CrudRepository<ProductEvent, Long> {

    @Query("SELECT r FROM ProductEvent r WHERE r.seq=:seq")
    Optional<ProductEvent> findBySeq(@Param("seq") Long seq);
}

