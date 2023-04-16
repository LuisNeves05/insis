using System.Text;
using MongoDB.Bson.IO;
using RabbitMQ.Client;
using VotesWrite.Constants;
using VotesWrite.Entities;

namespace VotesWrite.broker;

public static class RabitMQProducer
{
    public static void PublishMessage(byte[] message, string routingKey)
    {
        using var connection = RabbitMQConnection.Instance.GetConnection();
        if (!connection.IsOpen)
        {
            var newConnection = RabbitMQConnection.Instance.GetNewConnection();
            using var channel = newConnection.CreateModel();

            var properties = channel.CreateBasicProperties();
            properties.Persistent = true;
            channel.BasicPublish(exchange: BrokerConstants.exchangeName, routingKey: routingKey,
                basicProperties: properties, body: message);
        }
        else
        {
            using var channel = connection.CreateModel();

            var properties = channel.CreateBasicProperties();
            properties.Persistent = true;
            channel.BasicPublish(exchange: BrokerConstants.exchangeName, routingKey: routingKey,
                basicProperties: properties, body: message);
        }
    }
}