using System.Runtime.Serialization;
using System.Text;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using VotesWrite.broker;
using VotesWrite.Dtos.Events;
using VotesWrite.Interfaces.RepositoryInterfaces;
using VotesWrite.Interfaces.ServiceInterfaces;
using VotesWrite.Repositories.Votes;
using VotesWrite.Services;
using VotesWrite.Settings;
using Constants = VotesWrite.Constants.BrokerConstants;

var builder = WebApplication.CreateBuilder(args);
var policyName = "_myAllowSpecificOrigins";

// Add services to the container.
builder.Services.Configure<MongoDbSettings>(builder.Configuration.GetSection("MongoDb"));

// Dependency Injection
// ------------------------------ Votes ------------------------------------------//
builder.Services.AddSingleton<IVoteServices, VoteService>();
builder.Services.AddSingleton<IVotesRepository, VotesRepository>();

builder.Services.AddSingleton<IVoteRabbitServices, VoteServiceRabbit>();

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddCors(options =>
{
    options.AddPolicy(name: policyName,
        builderr =>
        {
            builderr
                .WithOrigins("http://localhost:3000")
                //.AllowAnyOrigin()
                .WithMethods("GET", "POST", "PUT", "DELETE")
                .AllowAnyHeader();
        });
});

/*
 * BROKER THINGS
 */

using var connection = RabbitMQConnection.Instance.GetConnection();
using var channel = connection.CreateModel();

// channel.ExchangeDeclare(exchange: exchangeName, type: ExchangeType.Direct);
var queueName = channel.QueueDeclare().QueueName;
channel.QueueBind(queue: queueName,
    exchange: Constants.exchangeName,
    routingKey: Constants.voteCreateRk);

channel.QueueBind(queue: queueName,
    exchange: Constants.exchangeName,
    routingKey: Constants.voteDeleteRk);

channel.QueueBind(queue: queueName,
    exchange: Constants.exchangeName,
    routingKey: Constants.voteUpdateRk);

var consumer = new EventingBasicConsumer(channel);
consumer.Received += (model, ea) =>
{
    var body = ea.Body.ToArray();
    var routingKey = ea.RoutingKey;
    VoteServiceRabbit p = new VoteServiceRabbit();

    p.DistributeViaRoutingKey(routingKey, body);
};
channel.BasicConsume(queue: "",
    autoAck: true,
    consumer: consumer);

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseCors(policyName);

// app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();