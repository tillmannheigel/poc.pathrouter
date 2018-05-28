package de.tillmannheigel.poc.pathrouter.webapplication.routesReporter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDescription;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.tillmannheigel.poc.pathrouter.webapplication.routesReporter.rules.Rule;

@Component
public class DynamicRoutesReporter {

    @Autowired
    List<Rule> rules;
    @Autowired
    private RequestMappingCollector requestMappingCollector;
    @Autowired
    private RoutesPublisher routesPublisher;

    @PostConstruct
    public void init() {
        List<DispatcherServletMappingDescription> requestMappingDescriptions = requestMappingCollector.getRequestMappingDescriptions();
        Set<Route> routes = rules.stream()
                .flatMap(rule -> rule.applyRule(requestMappingDescriptions).stream())
                .collect(Collectors.toSet());
        try {
            routesPublisher.publish(routes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
