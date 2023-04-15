using System.Runtime.Serialization;
using System.Text;
using RabbitMQ.Client.Events;
using VotesWrite.broker;
using VotesWrite.Constants;
using VotesWrite.Dtos.Create;
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

    public VoteServiceRabbit()
    {
    }

    public void DistributeViaRoutingKey(string routingKey, byte[] eventBytes)
    {
        using var stream = new MemoryStream(eventBytes);
        var serializer = new DataContractSerializer(typeof(CreateVoteEvent));
        var newEvent = serializer.ReadObject(stream) as CreateVoteEvent;

        switch (routingKey)
        {
            case BrokerConstants.voteCreateRk:
                if (newEvent != null) CreateVote(newEvent);
                break;
            case BrokerConstants.voteUpdateRk:
                if (newEvent != null) CreateVote(newEvent);
                break;
            case BrokerConstants.voteDeleteRk:
                if (newEvent != null) CreateVote(newEvent);
                break;
            default:
                if (newEvent != null) CreateVote(newEvent);
                break;
        }
    }

    public async void CreateVote(CreateVoteEvent voteEvent)
    {
        Vote newVote = new()
        {
            Type = voteEvent.Type,
            UserId = voteEvent.UserId
        };

        var result = await _voteRepository.Add(newVote);
        if (result is null) throw new ArgumentException();
    }
}