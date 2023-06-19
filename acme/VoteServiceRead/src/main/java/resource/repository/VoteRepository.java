package resource.repository;

import org.springframework.data.repository.CrudRepository;
import resource.model.Vote;


public interface VoteRepository extends CrudRepository<Vote, Long> {


}
