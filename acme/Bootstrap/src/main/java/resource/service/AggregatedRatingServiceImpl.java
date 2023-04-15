package resource.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resource.model.AggregatedRating;
import resource.model.Rating;
import resource.repository.AggregatedRatingRepository;
import resource.service.command_bus.CreateRatingCommand;

@Service
public class AggregatedRatingServiceImpl implements AggregatedRatingService{

    @Autowired
    AggregatedRatingRepository repository;

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
