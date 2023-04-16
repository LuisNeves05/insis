using Microsoft.AspNetCore.Mvc;
using VotesRead.Dtos.Create;
using VotesRead.Dtos.Response;
using VotesRead.Entities;
using VotesRead.Interfaces.ServiceInterfaces;

namespace VotesRead.Controllers;

[ApiController]
[Route("votes")]
public class VotesController : ControllerBase
{
    private readonly IVoteServices _voteServices;
    
    public VotesController(IVoteServices voteServices)
    {
        _voteServices = voteServices;
    }

    [HttpGet]
    public async Task<ActionResult<HephaestusResponse<List<VoteResponse>>>> GetVotes()
    {
        var data = await _voteServices.GetAllVotes();
        return data.Data is null ? StatusCode(500) : Ok(data);
    }
    
    [HttpGet("{id}")]
    public async Task<ActionResult<HephaestusResponse<List<VoteResponse>>>> GetVote(Guid id)
    {
        var data = await _voteServices.GetVote(id);
        return data.Data is null ? StatusCode(500) : Ok(data);
    }

}