using Microsoft.AspNetCore.Mvc;
using VotesWrite.Dtos.Create;
using VotesWrite.Dtos.Response;
using VotesWrite.Entities;
using VotesWrite.Interfaces.ServiceInterfaces;

namespace VotesWrite.Controllers;

[ApiController]
[Route("vote")]
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
    
    [HttpPost]
    public async Task<ActionResult<HephaestusResponse<VoteResponse>>> CreateVote(CreateVoteDto voteDto)
    {
        var data = await _voteServices.CreateVote(voteDto);
        return data.Data is null ? StatusCode(500) : Ok(data);
    }
}