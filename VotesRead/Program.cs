using System.Text.Json;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using VotesRead.broker;
using VotesRead.Interfaces.RepositoryInterfaces;
using VotesRead.Interfaces.ServiceInterfaces;
using VotesRead.Repositories.Votes;
using VotesRead.Services;
using VotesRead.Settings;
using Constants = VotesRead.Constants.BrokerConstants;

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

var app = builder.Build();

var consumer = new EventingBasicConsumer(channel);
consumer.Received += async (model, ea) =>
{
    var body = ea.Body.ToArray();
    var routingKey = ea.RoutingKey;
    var newEvent =  JsonSerializer.Deserialize<CreateVoteEvent>(body);
    var saveService = app.Services.GetService<IVoteRabbitServices>();

    switch (routingKey)
    {
        case Constants.voteCreateRk:
            if (saveService != null) saveService.CreateVote(newEvent);
            break;
        case Constants.voteUpdateRk:
            if (saveService != null) saveService.CreateVote(newEvent);
            break;
        case Constants.voteDeleteRk:
            if (saveService != null) saveService.DeleteVote(newEvent);
            break;
        default:
            Console.WriteLine($"Unknown routing key: {routingKey}");
            break;
    }
};

channel.BasicConsume(queue: "",
    autoAck: true,
    consumer: consumer, noLocal: true);


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