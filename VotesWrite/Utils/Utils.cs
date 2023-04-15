using VotesWrite.Dtos.Response;
using VotesWrite.Entities;

namespace VotesWrite.Utils;

public class Utils
{
    #region Vote Response
    public static VoteResponse toDto(Vote data)
    {
        return new VoteResponse()
        {
            Id = data.Id.ToString(),
            Type = data.Type,
        };
    }

    public static List<VoteResponse> toDto(List<Vote> data)
    {
        return (data.Select(elem => toDto(elem))).ToList();
    }

    #endregion
    
}