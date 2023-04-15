using System.Text;
using MongoDB.Bson.IO;
using RabbitMQ.Client;
using VotesRead.Constants;

namespace VotesRead.broker;

public class RabitMQProducer {
    public void PublishMessage(string message, string routingKey)
    {
        using var connection = RabbitMQConnection.Instance.GetConnection();
        using var channel = connection.CreateModel();

        var body = Encoding.UTF8.GetBytes(message);
        var properties = channel.CreateBasicProperties();
        properties.Persistent = true;
        channel.BasicPublish(exchange: BrokerConstants.exchangeName, routingKey: routingKey, basicProperties: properties, body: body);
    }
}