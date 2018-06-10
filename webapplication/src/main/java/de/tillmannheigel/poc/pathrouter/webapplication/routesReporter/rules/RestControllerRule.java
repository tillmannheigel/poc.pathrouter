package de.tillmannheigel.poc.pathrouter.webapplication.routesReporter.rules;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.tillmannheigel.poc.pathrouter.webapplication.routesReporter.RequestMappingCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDescription;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import de.tillmannheigel.poc.pathrouter.webapplication.routesReporter.Route;

@Component
public class RestControllerRule implements Rule {

    @Autowired
    private RequestMappingCollector requestMappingCollector;

    public List<Route> getRoutes() {
        List<DispatcherServletMappingDescription> requestMappingDescriptions = requestMappingCollector.getRequestMappingDescriptions();
        List<Route> routes = new ArrayList<>();

        for (DispatcherServletMappingDescription dispatcherServletMappingDescription : requestMappingDescriptions) {
            DispatcherServletMappingDetails details = dispatcherServletMappingDescription.getDetails();
            if (details != null) {
                String handlerMethodClass = details.getHandlerMethod().getClassName();
                Class<?> clazz;
                try {
                    clazz = Class.forName(handlerMethodClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }
                if (clazz != null) {
                    Annotation[] annotations = clazz.getAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof RestController) {
                            if (details.getRequestMappingConditions() != null
                                    && details.getRequestMappingConditions().getPatterns() != null) {
                                Set<String> patterns = details.getRequestMappingConditions().getPatterns();
                                patterns.forEach(s -> {
                                    routes.add(Route.builder().pathPattern(s).build());
                                });

                            }
                        }
                    }
                }

            }
        }
        return routes;
    }
}
