using VotesRead.Dtos.Create;
using VotesRead.Dtos.Response;
using VotesRead.Entities;

namespace VotesRead.Interfaces.ServiceInterfaces;

public interface IVoteServices
{
    Task<HephaestusResponse<List<VoteResponse>>> GetAllVotes();
    Task<HephaestusResponse<VoteResponse>> GetVote(Guid id);
}