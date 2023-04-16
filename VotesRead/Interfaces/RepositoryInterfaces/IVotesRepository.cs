
using VotesRead.Entities;

namespace VotesRead.Interfaces.RepositoryInterfaces;

public interface IVotesRepository : IBaseRepository<Vote>
{
    Task<Vote> GetVoteByUserIdAndReviewIdAsync(long userId, long reviewId);

}