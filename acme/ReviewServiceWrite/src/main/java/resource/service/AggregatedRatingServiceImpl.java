package resource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resource.model.AggregatedRating;
import resource.repository.AggregatedRatingRepository;

@Service
public class AggregatedRatingServiceImpl implements AggregatedRatingService{

    @Autowired
    AggregatedRatingRepository arRepository;

    // TODO message broker
    //@Autowired
    //private ProductRepository pRepository;

    @Autowired
    ReviewService rService;


    @Override
    public AggregatedRating save(String sku ) {

        // TODO message broker
        /*Optional<Product> product = pRepository.findBySku( sku );

        if (product.isEmpty()){
            return null;
        }

        Double average = rService.getWeightedAverage(product.get());


        Optional<AggregatedRating> r = arRepository.findByProductId(product.get());
        AggregatedRating aggregateF;

        if(r.isPresent()) {
            r.get().setAverage( average );
            aggregateF = arRepository.save(r.get());
        }
        else {
            AggregatedRating f = new AggregatedRating(average, product.get());
            aggregateF = arRepository.save(f);
        }

        return aggregateF;*/
        return null;
    }


}
