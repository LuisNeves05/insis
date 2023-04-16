namespace VotesRead.Interfaces.RepositoryInterfaces;

public interface IBaseRepository<TEntity>
{
    Task<List<TEntity>> GetAll();
    Task<TEntity?> Get(string id);
    Task<TEntity?> Add(TEntity newItem);

    Task Update(TEntity obj);
    Task Delete(string id);
}