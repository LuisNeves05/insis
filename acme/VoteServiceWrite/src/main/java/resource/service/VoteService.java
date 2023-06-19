package resource.service;

import resource.dto.VoteDTO;
import resource.dto.VoteDTOResponse;
import resource.service.command_bus.DeleteVoteCommand;
import resource.service.command_bus.VoteCommand;

public interface VoteService {


    Boolean deleteVote(Long reviewId);
    void deleteReview(DeleteVoteCommand dr);
    void create(final VoteCommand r);

    VoteDTOResponse create(final VoteDTO voteDTO);
}
