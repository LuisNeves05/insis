namespace VotesWrite.Constants;

public static class BrokerConstants
{
    public const string exchangeName = "exchange";
    public const string voteCreateRk = "create-vote";
    public const string voteDeleteRk = "delete-vote";
    public const string voteUpdateRk = "update-vote";
    public const string incompleteVoteRk = "incomplete-vote-created";
    public const string createReviewForIncompleteVote = "create-review-for-incomplete-vote";

}