using System.ComponentModel.DataAnnotations;

namespace VotesRead.Dtos.Create;

public class CreateVoteDto
{

    [Required]
    public string? Type { get; }
    
    [Required]
    public long UserId { get; }
    
    [Required]
    public long ReviewId { get; }

    public CreateVoteDto(string? type, long userId, long reviewId)
    {
        Type = type;
        UserId = userId;
        ReviewId = reviewId;
    }
}