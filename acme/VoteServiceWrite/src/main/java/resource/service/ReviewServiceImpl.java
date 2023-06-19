package resource.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resource.broker.RabbitMQConfig;
import resource.dto.VoteDTO;
import resource.dto.VoteDTOResponse;
import resource.model.Vote;
import resource.model.VoteMapper;
import resource.repository.VoteRepository;
import resource.service.command_bus.DeleteVoteCommand;
import resource.service.command_bus.VoteCommand;

import java.util.Optional;

@Service
@Transactional
public class ReviewServiceImpl implements VoteService {

    @Autowired
    VoteRepository repository;
    @Autowired
    private RabbitTemplate rabbitMessagingTemplate;

    @Override
    public Boolean deleteVote(Long reviewId) {

        Optional<Vote> rev = repository.findById(reviewId);

        if (rev.isEmpty()) {
            return null;
        }
        Vote r = rev.get();

        repository.delete(r);
        publishReviewMessage(serializeDeleteObject(r), RabbitMQConfig.VOTE_DELETE_RK);
        return true;

    }


    public void publishReviewMessage(byte[] payload, String routingKey) {
        this.rabbitMessagingTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                routingKey,
                payload);
    }

    private byte[] serializeCreateObject(Vote r) {
        VoteCommand event = new VoteCommand(r.getReviewId(),r.getUsername(),r.getStatus());
        return SerializationUtils.serialize(event);
    }

    private byte[] serializeDeleteObject(Vote r) {
        DeleteVoteCommand event = new DeleteVoteCommand(r.getReviewId());
        return SerializationUtils.serialize(event);
    }

    @Override
    public void create(final VoteCommand r) {
        Vote vote = new Vote(r.getUsername(), r.getStatus(),r.getReviewId());
        if (repository.findByReviewIdAndUsername(r.getReviewId(), r.getUsername()).isEmpty()) {
            repository.save(vote);
        }
    }

    @Override
    public VoteDTOResponse create(final VoteDTO voteDTO) {

        Vote vote = new Vote(voteDTO.getUsername(), voteDTO.getStatus(), voteDTO.getReviewId());

        vote = repository.save(vote);

        publishReviewMessage(serializeCreateObject(vote), RabbitMQConfig.VOTE_CREATE_RK);

        return VoteMapper.toDto(vote);
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
}