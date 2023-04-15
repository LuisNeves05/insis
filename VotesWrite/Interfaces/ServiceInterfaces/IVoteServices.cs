using VotesWrite.Dtos.Create;
using VotesWrite.Dtos.Response;
using VotesWrite.Entities;

namespace VotesWrite.Interfaces.ServiceInterfaces;

public interface IVoteServices
{
    Task<HephaestusResponse<List<VoteResponse>>> GetAllVotes();
    Task<HephaestusResponse<VoteResponse>> GetVote(Guid id);
    Task<HephaestusResponse<VoteResponse>> CreateVote(CreateVoteDto voteDto);
    Task<HephaestusResponse<VoteResponse>> UpdateVote(Guid id, CreateVoteDto voteDto);
    Task<HephaestusResponse<VoteResponse>> DeleteVote(Guid id);
}