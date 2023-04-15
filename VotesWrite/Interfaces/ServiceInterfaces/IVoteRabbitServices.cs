using VotesWrite.Dtos.Events;

namespace VotesWrite.Interfaces.ServiceInterfaces;

public interface IVoteRabbitServices
{
    void CreateVote(CreateVoteEvent voteEvent);

}