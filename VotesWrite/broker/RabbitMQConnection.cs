﻿using RabbitMQ.Client;

namespace VotesWrite.broker;

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
            Port = 5672
        };

        _connection = factory.CreateConnection();
    }

    public IConnection GetConnection()
    {
        return _connection;
    }
}