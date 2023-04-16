using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using VotesWrite.Entities;

namespace VotesWrite.Dtos.Create;

public class CreateVoteDto
{

    [Required]
    public string? Type { get; }
    
    [Required]
    public string? ProductSku { get; }

    [Required]
    public long UserId { get; }
    
    [Required]
    public long ReviewId { get; }
    
    [Required]
    public Vote.VoteStatus Status { get; set; }

    public CreateVoteDto(string? type, long userId, long reviewId, Vote.VoteStatus voteStatus, string productSku)
    {
        Type = type;
        UserId = userId;
        ReviewId = reviewId;
        Status = voteStatus;
        ProductSku = productSku;
    }
}