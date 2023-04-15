using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Runtime.Serialization;

namespace VotesWrite.Dtos.Events;

[DataContract]
public class CreateVoteEvent
{
   
    public string id { get; }
    
    [DataMember]
    public string? Type { get; }
    
    [DataMember]
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