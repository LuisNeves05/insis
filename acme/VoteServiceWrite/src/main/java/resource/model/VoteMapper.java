package resource.model;

import resource.dto.VoteDTO;
import resource.dto.VoteDTOResponse;

import java.util.ArrayList;
import java.util.List;

public class VoteMapper {

    public static VoteDTOResponse toDto(Vote vote){
        return new VoteDTOResponse(vote.getVoteId(), vote.getUsername(), vote.getReviewId(), vote.getStatus());
    }

    public static List<VoteDTOResponse> toDtoList(List<Vote> votes) {
        List<VoteDTOResponse> dtoList = new ArrayList<>();

        for (Vote rev: votes) {
            dtoList.add(toDto(rev));
        }
        return dtoList;
    }
}
