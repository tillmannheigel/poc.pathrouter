package de.tillmannheigel.poc.pathrouter.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterApplicationConfiguration {

    @Autowired
    protected ZuulProperties zuulProperties;

    @Autowired
    protected ServerProperties server;

    @Autowired(required = false)
    private Registration registration;

    @Autowired
    private DiscoveryClient discovery;

    @Autowired
    private ServiceRouteMapper serviceRouteMapper;

    @Bean
    public NoPrefixDiscoveryClientRouteLocator discoveryRouteLocator() {
        return new NoPrefixDiscoveryClientRouteLocator(this.server.getServlet().getServletPrefix(), this.discovery, this.zuulProperties,
                this.serviceRouteMapper, this.registration);
    }

}
