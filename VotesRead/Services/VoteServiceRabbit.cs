using VotesRead.Dtos.Response;
using VotesRead.Entities;
using VotesRead.Interfaces.RepositoryInterfaces;
using VotesRead.Interfaces.ServiceInterfaces;
using Constants = VotesRead.Constants.BrokerConstants;

namespace VotesRead.Services;

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

        if (_voteRepository.GetVoteByUserIdAndReviewIdAsync(voteEvent.UserId, voteEvent.ReviewId) != null)
        {
            Vote newVote = new()
            {
                Type = voteEvent?.Type,
                UserId = voteEvent!.UserId,
                ReviewId = voteEvent.ReviewId
            };

            var result = await _voteRepository.Add(newVote);
            if (result is null) throw new ArgumentException("Could not create vote");
        }
    }
    
    public async void DeleteVote(CreateVoteEvent? voteEvent)
    {
        Console.WriteLine("######################################");
        Console.WriteLine("Received Delete Message");
        Console.WriteLine("######################################");

        await _voteRepository.Delete(voteEvent!.Id);
    }
}