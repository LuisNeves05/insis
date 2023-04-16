namespace VotesWrite.Dtos.Events;

public class UpdateVoteEvent
{
    public Guid VoteId { get; set; }
    public long ReviewId { get; set; }

    public UpdateVoteEvent(Guid voteId, long reviewId)
    {
        VoteId = voteId;
        ReviewId = reviewId;
    }
}