﻿using VotesWrite.Dtos.Events;
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