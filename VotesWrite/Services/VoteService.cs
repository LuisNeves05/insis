using System.Text.Json;
using Microsoft.VisualBasic.CompilerServices;
using VotesWrite.broker;
using VotesWrite.Dtos.Create;
using VotesWrite.Dtos.Response;
using VotesWrite.Entities;
using VotesWrite.Interfaces.RepositoryInterfaces;
using VotesWrite.Interfaces.ServiceInterfaces;

namespace VotesWrite.Services;

public class VoteService : IVoteServices
{
    private readonly IVotesRepository _voteRepository;

    public VoteService(IVotesRepository voteRepository)
    {
        _voteRepository = voteRepository;
    }

    public async Task<HephaestusResponse<VoteResponse>> CreateVote(CreateVoteDto voteDto)
    {
        
        try
        {
            Vote newVote = new()
            {
                Type = voteDto.Type,
                UserId = voteDto.UserId
            };
            
            var result = await _voteRepository.Add(newVote);

            if (result is null) throw new ArgumentException();
            CreateVoteEvent createVoteEvent = new CreateVoteEvent(result.Id, result.Type, result.UserId, result.ReviewId);
            
            var messageBody = JsonSerializer.SerializeToUtf8Bytes(createVoteEvent);

            RabitMQProducer.PublishMessage(messageBody, Constants.BrokerConstants.voteCreateRk);
            return new HephaestusResponse<VoteResponse>().SetSucess(Utils.Utils.toDto(result), 1);
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex);
            return new HephaestusResponse<VoteResponse>().SetFail(ex);
        }
    }

    public async Task<HephaestusResponse<VoteResponse>> DeleteVote(Guid id)
    {
        try
        {
            var client = await _voteRepository.Get(id);
            if (client is null) throw new ArgumentException();

            await _voteRepository.Delete(id);
            var result = await _voteRepository.Get(id);

            if (result != null) throw new ArgumentException();
            var messageBody = JsonSerializer.SerializeToUtf8Bytes(result);
            RabitMQProducer.PublishMessage(messageBody, Constants.BrokerConstants.voteDeleteRk);
            return new HephaestusResponse<VoteResponse>().SetSucess();
        }
        catch (Exception ex)
        {
            return new HephaestusResponse<VoteResponse>().SetFail(ex);
        }
    }

    public async Task<HephaestusResponse<VoteResponse>> UpdateVote(Guid id, CreateVoteDto clientDto)
    {
        try
        {
            var vote = await _voteRepository.Get(id);
            if (vote is null) throw new ArgumentException("Vote not Found with that specific id");

            vote.Type = clientDto.Type;
            vote.UserId = clientDto.UserId;
            vote.Changed = DateTime.Now;

            await _voteRepository.Update(vote);

            var result = await _voteRepository.Get(id);
            if (result is null) throw new ArgumentException();
            var messageBody = JsonSerializer.SerializeToUtf8Bytes(result);
            RabitMQProducer.PublishMessage(messageBody, Constants.BrokerConstants.voteUpdateRk);

            return new HephaestusResponse<VoteResponse>().SetSucess(Utils.Utils.toDto(result), 1);
        }
        catch (Exception ex)
        {
            return new HephaestusResponse<VoteResponse>().SetFail(ex);
        }
    }
}