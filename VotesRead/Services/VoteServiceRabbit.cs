using System.Runtime.Serialization;
using System.Text;
using RabbitMQ.Client.Events;
using VotesRead.Dtos.Events;
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

    public async Task<HephaestusResponse<VoteResponse>> CreateVote(CreateVoteEvent? voteEvent)
    {
        Console.WriteLine("ENTROU AQUI AAAEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        Vote newVote = new()
        {
            Type = voteEvent?.Type,
            UserId = voteEvent!.UserId
        };

        var result = await _voteRepository.Add(newVote);
        if (result is null) throw new ArgumentException();
        return new HephaestusResponse<VoteResponse>().SetSucess(Utils.Utils.toDto(result), 1);
    }
}