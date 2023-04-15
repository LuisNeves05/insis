package resource.repository;

import org.springframework.data.repository.CrudRepository;
import resource.model.ProdImage;

public interface ImageRepository extends CrudRepository<ProdImage, Long> {
}