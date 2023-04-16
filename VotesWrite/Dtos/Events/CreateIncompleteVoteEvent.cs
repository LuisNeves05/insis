namespace VotesWrite.Dtos.Events;

public class CreateIncompleteVoteEvent
{
    
    public string? ProductSku { get; }
    
    public long UserId { get; }
    
    public Guid VoteId { get; }


    public CreateIncompleteVoteEvent(string? productSku, long userId, Guid voteId)
    {
        ProductSku = productSku;
        UserId = userId;
        VoteId = voteId;
    }
}