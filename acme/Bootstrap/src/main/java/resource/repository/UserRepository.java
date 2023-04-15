package resource.repository;


import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import resource.controller.ResourceNotFoundException;
import resource.model.UserR;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "users")
public interface UserRepository extends CrudRepository<UserR, Long> {

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.userId", condition = "#p0.userId != null"),
            @CacheEvict(key = "#p0.username", condition = "#p0.username != null") })
    <S extends UserR> S save(S entity);

    @Override
    @Cacheable
    Optional<UserR> findById(Long userId);

    @Cacheable
    default UserR getById(final Long userId){
        final Optional<UserR> optionalUser = findById(userId);

        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException(UserR.class, userId);
        }

        return optionalUser.get();
    }

    @Cacheable
    Optional<UserR> findByUsername(String username);


}