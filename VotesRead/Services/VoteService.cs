using VotesRead.Dtos.Response;
using VotesRead.Entities;
using VotesRead.Interfaces.RepositoryInterfaces;
using VotesRead.Interfaces.ServiceInterfaces;

namespace VotesRead.Services;

public class VoteService : IVoteServices
{
    private readonly IVotesRepository _voteRepository;

    public VoteService(IVotesRepository voteRepository)
    {
        _voteRepository = voteRepository;
    }
    
    public async Task<HephaestusResponse<List<VoteResponse>>> GetAllVotes()
    {
        try
        {
            var result = await _voteRepository.GetAll();
            if (result is null) throw new ArgumentException();

            return new HephaestusResponse<List<VoteResponse>>().SetSucess(Utils.Utils.toDto(result), result.Count);
        }
        catch (Exception ex)
        {
            return new HephaestusResponse<List<VoteResponse>>().SetFail(ex);
        }
    }

    public async Task<HephaestusResponse<VoteResponse>> GetVote(Guid id)
    {
        try
        {
            var result = await _voteRepository.Get(id);
            if (result is null) throw new ArgumentException();

            return new HephaestusResponse<VoteResponse>().SetSucess(Utils.Utils.toDto(result), 1);
        }
        catch (Exception ex)
        {
            return new HephaestusResponse<VoteResponse>().SetFail(ex);
        }
    }
}