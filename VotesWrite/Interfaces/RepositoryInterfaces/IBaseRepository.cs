namespace VotesWrite.Interfaces.RepositoryInterfaces;

public interface IBaseRepository<TEntity>
{
    Task<List<TEntity>> GetAll();
    Task<TEntity?> Get(Guid id);
    Task<TEntity?> Add(TEntity newItem);

    Task Update(TEntity obj);
    Task Delete(Guid id);
}