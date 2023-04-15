namespace VotesWrite.Entities;

public class Vote : Entity
{
    public string? Type { get; set; }
    
    public long UserId { get; set; }

}