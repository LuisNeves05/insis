namespace VotesRead.Dtos.Response;

public class VoteResponse
{
    public string? Id { get; set; }
    public string? Type { get; set; }

    public long UserId { get; set; }
    
    public long ReviewId { get; set; }
}