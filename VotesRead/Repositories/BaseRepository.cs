using Microsoft.Extensions.Options;
using MongoDB.Driver;
using VotesRead.Entities;
using VotesRead.Interfaces.RepositoryInterfaces;
using VotesRead.Settings;

namespace VotesRead.Repositories;

public class BaseBaseRepository<TEntity> : IBaseRepository<TEntity> where TEntity : Entity
{
    protected IMongoCollection<TEntity> DataCollection { get; } = null!;

    protected BaseBaseRepository(IOptions<MongoDbSettings> dataStoreDatabaseSettings, string collection)
    {
        var mongoClient = new MongoClient(
            dataStoreDatabaseSettings.Value.ConnectionString);

        var mongoDatabase = mongoClient.GetDatabase(
            dataStoreDatabaseSettings.Value.DatabaseName);

        DataCollection = collection switch
        {
            "Votes" => mongoDatabase.GetCollection<TEntity>(dataStoreDatabaseSettings.Value.VotesCollectionName),
            _ => DataCollection
        };
    }


    public async Task<List<TEntity>> GetAll()
    {
        var data = await DataCollection.Find(_ => true).ToListAsync();
        return data;
    }

    public async Task<TEntity?> Get(Guid id)
    {
        var data = await DataCollection.Find(x => x.Id.Equals(id)).FirstOrDefaultAsync();
        return data;
    }

    public async Task<TEntity?> Add(TEntity newData)
    {
        await DataCollection.InsertOneAsync(newData);
        var dataCreated = await Get(newData.Id);
        return dataCreated ?? null;
    }


    public async Task Update(TEntity updatedData)
    {
        var id = updatedData.Id;
        await DataCollection.ReplaceOneAsync(x => x.Id == id, updatedData);
    }


    public async Task Delete(Guid id)
    {
        await DataCollection.DeleteOneAsync(x => x.Id == id);
    }
}