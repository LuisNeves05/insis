package resource.service;


import resource.dto.VoteDTOResponse;
import resource.service.command_bus.DeleteVoteCommand;
import resource.service.command_bus.VoteCommand;

import java.util.List;

public interface VoteService {

    List<VoteDTOResponse> findAllVotes();

    void deleteReview(DeleteVoteCommand dr);

    void create(final VoteCommand r);
}
