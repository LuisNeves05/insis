# Use the official .NET 7 SDK image as the base image
FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build-env

# Set the working directory in the container
WORKDIR /app

# Copy the project file to the container
COPY *.csproj ./

# Restore the NuGet packages
RUN dotnet restore

# Copy the source code to the container
COPY . ./

# Build the application
RUN dotnet publish -c Release -o out

# Create a new image with the application
FROM mcr.microsoft.com/dotnet/runtime:7.0
WORKDIR /app
COPY --from=build-env /app/out .
ENTRYPOINT ["dotnet", "run"]
