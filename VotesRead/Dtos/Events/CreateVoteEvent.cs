using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Runtime.Serialization;

namespace VotesRead.Dtos.Events;

public class CreateVoteEvent
{
   
    public Guid Id { get; }
    
    public string? Type { get; }
    
    public long UserId { get; }
    
    public long ReviewId { get; }

    public CreateVoteEvent(Guid id, string? type, long userId, long reviewId)
    {
        Id = id;
        Type = type;
        UserId = userId;
        ReviewId = reviewId;
    }
    
}