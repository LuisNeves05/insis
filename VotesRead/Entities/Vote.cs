namespace VotesRead.Entities;

public class Vote : Entity
{
    public string? Type { get; set; }
    
    public long UserId { get; set; }
    
    public long ReviewId { get; set; }

}