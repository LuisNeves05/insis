using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace VotesRead.Entities;

public class Entity
{
    public Guid Id { get; init; }

    public DateTime Created { get; set; }
    public DateTime Changed { get; set; }


    protected Entity()
    {
        Id = Guid.NewGuid();
        Created = DateTime.Now;
        Changed = DateTime.Now;
    }
}