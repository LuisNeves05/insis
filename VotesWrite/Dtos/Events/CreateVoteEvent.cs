using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Runtime.Serialization;

namespace VotesWrite.Dtos.Events;

[DataContract]
public class CreateVoteEvent
{
   
    [DataMember]
    public string? Type { get; }
    
    [DataMember]
    public long UserId { get; }

    public CreateVoteEvent(string? type, long userId)
    {
        Type = type;
        UserId = userId;
    }
}