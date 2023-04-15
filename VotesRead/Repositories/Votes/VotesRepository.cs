﻿using Microsoft.Extensions.Options;
using MongoDB.Driver;
using VotesRead.Entities;
using VotesRead.Interfaces.RepositoryInterfaces;
using VotesRead.Settings;

namespace VotesRead.Repositories.Votes;

public class VotesRepository : BaseBaseRepository<Vote>, IVotesRepository
{
    
    private readonly IMongoCollection<Vote> _votesCollection;
    
    public VotesRepository(IOptions<MongoDbSettings> dataStoreDatabaseSettings) : 
        base(dataStoreDatabaseSettings, dataStoreDatabaseSettings.Value.VotesCollectionName)
    {
        _votesCollection = DataCollection;
    }


}