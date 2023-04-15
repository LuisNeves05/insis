using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace VotesRead.Dtos.Create;

public class CreateVoteDto
{

    [Required]
    public string? Type { get; }
    
    [Required]
    public long UserId { get; }

    public CreateVoteDto(string? type, long userId)
    {
        Type = type;
        UserId = userId;
    }
}