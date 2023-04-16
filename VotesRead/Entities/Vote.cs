namespace VotesRead.Entities;

public class Vote : Entity
{
    public enum VoteStatus
    {
        Complete,
        Incomplete
    }
    public string? Type { get; set; }
    
    public long UserId { get; set; }
    
    public long ReviewId { get; set; }
    
    public VoteStatus Status { get; set; }

    
    

}