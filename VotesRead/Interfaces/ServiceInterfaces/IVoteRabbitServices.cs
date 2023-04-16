namespace VotesRead.Interfaces.ServiceInterfaces;

public interface IVoteRabbitServices
{
    void CreateVote(CreateVoteEvent? voteEvent);
    void DeleteVote(CreateVoteEvent? voteEvent);
}