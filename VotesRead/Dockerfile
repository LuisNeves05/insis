FROM mcr.microsoft.com/dotnet/aspnet:7.0 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build
WORKDIR /src
COPY ["VotesRead/VotesRead.csproj", "VotesRead/"]
RUN dotnet restore "VotesRead/VotesRead.csproj"
COPY . .
WORKDIR "/src/VotesRead"
RUN dotnet build "VotesRead.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "VotesRead.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "VotesRead.dll"]
