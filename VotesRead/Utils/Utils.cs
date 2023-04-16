using VotesRead.Dtos.Response;
using VotesRead.Entities;

namespace VotesRead.Utils;

public class Utils
{
    #region Vote Response

    public static VoteResponse toDto(Vote data)
    {
        return new VoteResponse()
        {
            Id = data.Id.ToString(),
            Type = data.Type,
            ReviewId = data.ReviewId,
            UserId = data.UserId
        };
    }

    public static List<VoteResponse> toDto(List<Vote> data)
    {
        return (data.Select(elem => toDto(elem))).ToList();
    }

    #endregion
}