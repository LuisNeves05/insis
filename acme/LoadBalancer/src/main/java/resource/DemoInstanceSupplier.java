package resource;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

class DemoInstanceSupplier implements ServiceInstanceListSupplier {
    private final String serviceId;

    public DemoInstanceSupplier(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return Flux.just(Arrays
                .asList(new DefaultServiceInstance(serviceId + "1", serviceId, "localhost", 8081, false),
                        new DefaultServiceInstance(serviceId + "2", serviceId, "localhost", 8082, false),
                        new DefaultServiceInstance(serviceId + "3", serviceId, "localhost", 8083, false),
                        new DefaultServiceInstance(serviceId + "4", serviceId, "localhost", 8084, false),
                        new DefaultServiceInstance(serviceId + "5", serviceId, "localhost", 8085, false),
                        new DefaultServiceInstance(serviceId + "6", serviceId, "localhost", 8086, false)));
    }
}