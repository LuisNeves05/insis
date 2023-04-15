using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Runtime.Serialization;

namespace VotesWrite.Dtos.Events;

public class CreateVoteEvent
{
   
    public string id { get; }
    
    public string? Type { get; }
    
    public long UserId { get; }

    public CreateVoteEvent(string id, string? type, long userId)
    {
        this.id = id;
        Type = type;
        UserId = userId;
    }

    public CreateVoteEvent()
    {
    }
}