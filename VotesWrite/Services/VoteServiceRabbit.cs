using System.Diagnostics;
using System.Text.Json;
using VotesWrite.broker;
using VotesWrite.Dtos.Events;
using VotesWrite.Dtos.Response;
using VotesWrite.Entities;
using VotesWrite.Interfaces.RepositoryInterfaces;
using VotesWrite.Interfaces.ServiceInterfaces;
using Constants = VotesWrite.Constants.BrokerConstants;

namespace VotesWrite.Services;

public class VoteServiceRabbit : IVoteRabbitServices
{
    private readonly IVotesRepository _voteRepository;

    public VoteServiceRabbit(IVotesRepository voteRepository)
    {
        _voteRepository = voteRepository;
    }

    public async void CreateVote(CreateVoteEvent? voteEvent)
    {
        Console.WriteLine("######################################");
        Console.WriteLine("Received Create Message");
        Console.WriteLine("######################################");

        if (_voteRepository.GetVoteByUserIdAndReviewIdAsync(voteEvent.UserId, voteEvent.ReviewId) == null)
        {
            Console.WriteLine("Vote did not exist previously");
            Vote newVote = new()
            {
                Type = voteEvent?.Type,
                UserId = voteEvent!.UserId,
                ReviewId = voteEvent.ReviewId
            };

            var result = await _voteRepository.Add(newVote);
            if (result is null) throw new ArgumentException("Could not create vote");
        }
        else
        {
            Console.WriteLine("Vote already existed");
        }
    }

    public async void DeleteVote(CreateVoteEvent? voteEvent)
    {
        Console.WriteLine("######################################");
        Console.WriteLine("Received Delete Message");
        Console.WriteLine("######################################");

        await _voteRepository.Delete(voteEvent!.Id);
    }

    public async void UpdateIncompleteVote(UpdateVoteEvent? voteEvent)
    {
        try
        {
            Debug.Assert(voteEvent != null, nameof(voteEvent) + " != null");
            var vote = await _voteRepository.Get(voteEvent.VoteId);
            if (vote is null) throw new ArgumentException("Vote not Found with that specific id");

            if (voteEvent.ReviewId != 0)
            {
                vote.ReviewId = voteEvent.ReviewId;
                vote.Status = Vote.VoteStatus.Complete;

                await _voteRepository.Update(vote);

                var result = await _voteRepository.Get(vote.Id);
                if (result is null) throw new ArgumentException();
                var messageBody = JsonSerializer.SerializeToUtf8Bytes(result);
                RabitMQProducer.PublishMessage(messageBody, Constants.BrokerConstants.voteCreateRk);
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("Erro no update vote: \n" + ex);
        }
    }
}