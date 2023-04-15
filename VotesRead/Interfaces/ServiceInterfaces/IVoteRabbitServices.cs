using VotesRead.Dtos.Response;
using VotesRead.Entities;
using VotesWrite.Dtos.Events;


namespace VotesRead.Interfaces.ServiceInterfaces;

public interface IVoteRabbitServices
{
    Task<HephaestusResponse<VoteResponse>> CreateVote(CreateVoteEvent? voteEvent);
}