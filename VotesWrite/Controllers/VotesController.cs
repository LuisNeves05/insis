using Microsoft.AspNetCore.Mvc;
using VotesWrite.Dtos.Create;
using VotesWrite.Dtos.Response;
using VotesWrite.Entities;
using VotesWrite.Interfaces.ServiceInterfaces;

namespace VotesWrite.Controllers;

[ApiController]
[Route("votes")]
public class VotesController : ControllerBase
{
    private readonly IVoteServices _voteServices;
    
    public VotesController(IVoteServices voteServices)
    {
        _voteServices = voteServices;
    }

    [HttpPost]
    public async Task<ActionResult<HephaestusResponse<VoteResponse>>> CreateVote(CreateVoteDto voteDto)
    {
        var data = await _voteServices.CreateVote(voteDto);
        return data.Data is null ? StatusCode(500) : Ok(data);
    }
    [HttpDelete]
    public async Task<ActionResult<HephaestusResponse<VoteResponse>>> DeleteVote(Guid id)
    {
        var data = await _voteServices.DeleteVote(id);
        return data.Data is null ? StatusCode(500) : Ok(data);
    }
    [HttpPut]
    public async Task<ActionResult<HephaestusResponse<VoteResponse>>> UpdateVote(Guid id, CreateVoteDto voteDto)
    {
        var data = await _voteServices.UpdateVote(id, voteDto);
        return data.Data is null ? StatusCode(500) : Ok(data);
    }
}