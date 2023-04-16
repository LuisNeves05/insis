using RabbitMQ.Client;
using RabbitMQ.Client.Events;

namespace VotesRead.broker;

public sealed class RabbitMQConnection
{
    private static readonly RabbitMQConnection _instance = new();

    public static RabbitMQConnection Instance => _instance;

    private readonly IConnection _connection;

    private RabbitMQConnection()
    {
        var factory = new ConnectionFactory
        {
            HostName = "localhost",
            UserName = "guest",
            Password = "guest",
            Port = 5672,
            AutomaticRecoveryEnabled = true
        };

        _connection = factory.CreateConnection();
    }

    public IConnection GetConnection()
    {
        return _connection;
    }

    public IConnection GetNewConnection()
    {
        var factory = new ConnectionFactory
        {
            HostName = "localhost",
            UserName = "guest",
            Password = "guest",
            Port = 5672,
            AutomaticRecoveryEnabled = true
        };

        return factory.CreateConnection();
    }
}