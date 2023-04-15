namespace VotesRead.Settings;

public class MongoDbSettings {

    public string ConnectionString  { get; set; } = null!;
    public string DatabaseName { get; set; } = null!;
    public string VotesCollectionName { get; set; } = null!;

}