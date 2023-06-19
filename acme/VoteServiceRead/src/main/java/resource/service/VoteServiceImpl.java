package resource.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resource.dto.VoteDTOResponse;
import resource.model.Vote;
import resource.repository.VoteRepository;
import resource.service.command_bus.DeleteVoteCommand;
import resource.service.command_bus.VoteCommand;

import java.util.List;
import java.util.Optional;

import static resource.model.VoteMapper.toDtoList;

@Service
@Transactional
public class VoteServiceImpl implements VoteService {

    @Autowired
    VoteRepository repository;

    @Override
    public List<VoteDTOResponse> findAllVotes() {
        List<Vote> r = (List<Vote>) repository.findAll();

        return toDtoList(r);
    }

    @Override
    public void deleteReview(DeleteVoteCommand dr) {

        Optional<Vote> rev = repository.findById(dr.voteId());

        if (rev.isEmpty()) {
            return;
        }
        Vote r = rev.get();
        repository.delete(r);
    }

    @Override
    public void create(final VoteCommand r) {
        Vote vote = new Vote(r.getUsername(), r.getStatus(),r.getReviewId());
        repository.save(vote);
    }

}