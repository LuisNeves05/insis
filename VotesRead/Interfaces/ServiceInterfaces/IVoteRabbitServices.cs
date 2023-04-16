using VotesRead.Dtos.Response;
using VotesRead.Entities;
using VotesRead.Dtos.Events;


namespace VotesRead.Interfaces.ServiceInterfaces;

public interface IVoteRabbitServices
{
    void CreateVote(CreateVoteEvent? voteEvent);
    void DeleteVote(CreateVoteEvent? voteEvent);
}