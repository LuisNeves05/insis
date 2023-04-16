FROM mcr.microsoft.com/dotnet/aspnet:7.0 AS base
WORKDIR /app
EXPOSE 8087
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build
WORKDIR /src
COPY ["VotesWrite/VotesWrite.csproj", "VotesWrite/"]
RUN dotnet restore "VotesWrite/VotesWrite.csproj"
COPY . .
WORKDIR "/src/VotesWrite"
RUN dotnet build "VotesWrite.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "VotesWrite.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "VotesWrite.dll"]
