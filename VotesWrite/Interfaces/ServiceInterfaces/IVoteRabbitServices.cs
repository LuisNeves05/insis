using VotesWrite.Dtos.Events;
using VotesWrite.Dtos.Response;
using VotesWrite.Entities;

namespace VotesWrite.Interfaces.ServiceInterfaces;

public interface IVoteRabbitServices
{
    Task<HephaestusResponse<VoteResponse>> CreateVote(CreateVoteEvent? voteEvent);
}