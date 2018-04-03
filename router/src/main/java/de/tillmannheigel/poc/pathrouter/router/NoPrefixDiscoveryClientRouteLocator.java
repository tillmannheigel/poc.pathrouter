package de.tillmannheigel.poc.pathrouter.router;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.cloud.netflix.zuul.filters.discovery.SimpleServiceRouteMapper;

public class NoPrefixDiscoveryClientRouteLocator extends SimpleRouteLocator
        implements RefreshableRouteLocator {

    private DiscoveryClient discovery;

    private ZuulProperties properties;

    private ServiceRouteMapper serviceRouteMapper;

    public NoPrefixDiscoveryClientRouteLocator(String servletPath, DiscoveryClient discovery,
            ZuulProperties properties, ServiceInstance localServiceInstance) {
        super(servletPath, properties);

        if (properties.isIgnoreLocalService() && localServiceInstance != null) {
            String localServiceId = localServiceInstance.getServiceId();
            if (!properties.getIgnoredServices().contains(localServiceId)) {
                properties.getIgnoredServices().add(localServiceId);
            }
        }
        this.serviceRouteMapper = new SimpleServiceRouteMapper();
        this.discovery = discovery;
        this.properties = properties;
    }

    public NoPrefixDiscoveryClientRouteLocator(String servletPath, DiscoveryClient discovery,
            ZuulProperties properties, ServiceRouteMapper serviceRouteMapper, ServiceInstance localServiceInstance) {
        this(servletPath, discovery, properties, localServiceInstance);
        this.serviceRouteMapper = serviceRouteMapper;
    }

    @Override
    public void refresh() {
        System.out.println("NoPrefixDiscoveryClientRouteLocator#refresh()");
    }
}
