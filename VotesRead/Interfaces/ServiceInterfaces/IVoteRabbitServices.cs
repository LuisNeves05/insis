using VotesRead.Dtos.Events;
using VotesRead.Dtos.Response;
using VotesRead.Entities;


namespace VotesRead.Interfaces.ServiceInterfaces;

public interface IVoteRabbitServices
{
    Task<HephaestusResponse<VoteResponse>> CreateVote(CreateVoteEvent? voteEvent);
}