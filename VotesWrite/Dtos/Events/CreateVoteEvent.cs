
namespace VotesWrite.Dtos.Events;

public class CreateVoteEvent
{
   
    public string Id { get; }
    
    public string? Type { get; }
    
    public long UserId { get; }

    public CreateVoteEvent(string id, string? type, long userId)
    {
        Id = id;
        Type = type;
        UserId = userId;
    }
}