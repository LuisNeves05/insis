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

static IServiceCollection ConfigureServices()
{
    var services = new ServiceCollection();

    services.AddSingleton<IVoteRabbitServices, VoteServiceRabbit>();
    services.AddSingleton<Program>();

    return services;
}

static T Configure<T>(T service)
{
    // perform additional configuration if needed
    return service;
}

var services = ConfigureServices(); // configure services
var serviceProvider = services.BuildServiceProvider(); // build service provider
var consumer = new EventingBasicConsumer(channel);
consumer.Received += async (model, ea) =>
{
    var body = ea.Body.ToArray();
    var routingKey = ea.RoutingKey;
    using var stream = new MemoryStream(body);
    var serializer = new DataContractSerializer(typeof(CreateVoteEvent));
    var newEvent = serializer.ReadObject(stream) as CreateVoteEvent;
    Console.WriteLine("AQUI AQUI 1");
    switch (routingKey)
    {
        case Constants.voteCreateRk:
            Console.WriteLine("AQUI AQUI 2");
            var saveService = serviceProvider.GetRequiredService<IVoteRabbitServices>();
            await Configure(saveService).CreateVote(newEvent);
            break;
        case Constants.voteUpdateRk:
            var updateService = serviceProvider.GetRequiredService<IVoteRabbitServices>();
            // await Configure(updateService).UpdateAsync(newEvent);
            break;
        case Constants.voteDeleteRk:
            var deleteService = serviceProvider.GetRequiredService<IVoteRabbitServices>();
            // await Configure(deleteService).DeleteAsync(newEvent);
            break;
        default:
            Console.WriteLine($"Unknown routing key: {routingKey}");
            break;
    }
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