using VotesRead.Entities;

public class CreateVoteEvent
{
   
    public Guid Id { get; }
    
    public string? Type { get; }
    
    public long UserId { get; }
    
    public long ReviewId { get; }
    
    public Vote.VoteStatus Status { get; set; }

    public CreateVoteEvent(Guid id, string? type, long userId, long reviewId, Vote.VoteStatus status)
    {
        Id = id;
        Type = type;
        UserId = userId;
        ReviewId = reviewId;
        Status = status;
    }
    
}