
using VotesWrite.Entities;

namespace VotesWrite.Interfaces.RepositoryInterfaces;

public interface IVotesRepository : IBaseRepository<Vote>
{
    Task<Vote> GetVoteByUserIdAndReviewIdAsync(long userId, long reviewId);

}