package resource.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import resource.model.Vote;

import java.util.List;
import java.util.Optional;


public interface VoteRepository extends CrudRepository<Vote, Long> {

    @Query("SELECT r FROM Vote r WHERE r.reviewId=:reviewId and r.username=:username")
    Optional<List<Vote>> findByReviewIdAndUsername(@Param("reviewId")Long reviewId, @Param("username")String username);
}
