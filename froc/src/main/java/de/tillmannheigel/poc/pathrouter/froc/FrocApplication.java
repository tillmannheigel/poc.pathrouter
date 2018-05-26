package de.tillmannheigel.poc.pathrouter.froc;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class FrocApplication {


    public static final String FROC_ROUTES = "provided_paths";
    public static final String META = "/meta";
    @Value("${test.uri:http://httpbin.org/get}")
    String uri;

    @Autowired
    private RouteLocatorBuilder routeLocatorBuilder;

    @Autowired
    private DiscoveryClient discoveryClient;


    @Bean
    public RouteLocator staticRoutes() {
        return routeLocatorBuilder.routes()
                .route(r -> r.path("/static_hallo").uri("http://localhost:8080/"))
                .route(r -> r.path("/me").uri(uri))
                .build();
    }

    @Bean
    public RouteLocator metaDataRoutes() {
        RouteLocatorBuilder.Builder routesBuilder = routeLocatorBuilder.routes();
        discoveryClient.getServices().stream()
                .map(discoveryClient::getInstances)
                .filter(instances -> !instances.isEmpty())
                .map(instances -> instances.get(0))
                .forEach(serviceInstance -> {
                    Map<String, String> metadata = serviceInstance.getMetadata();
                    if (metadata.containsKey(FROC_ROUTES)) {
                        String[] paths = metadata.get(FROC_ROUTES).split(",");
                        for (String path : paths) {
                            routesBuilder.route(r -> r.path(path).uri(serviceInstance.getUri().resolve(path)));
                        }
                    }
                });

        return routesBuilder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(FrocApplication.class, args);
    }
}
