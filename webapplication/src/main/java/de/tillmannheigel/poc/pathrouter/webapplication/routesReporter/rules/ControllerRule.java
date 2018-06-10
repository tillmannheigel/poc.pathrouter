package de.tillmannheigel.poc.pathrouter.webapplication.routesReporter.rules;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDescription;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import de.tillmannheigel.poc.pathrouter.webapplication.routesReporter.Route;

@Component
@Slf4j
public class ControllerRule implements Rule {

    public List<Route> applyRule(List<DispatcherServletMappingDescription> servletMappingDescriptions) {
        List<Route> routes = new ArrayList<>();

        for (DispatcherServletMappingDescription dispatcherServletMappingDescription : servletMappingDescriptions) {
            DispatcherServletMappingDetails details = dispatcherServletMappingDescription.getDetails();
            if (details != null) {
                String handlerMethodClass = details.getHandlerMethod().getClassName();
                Class<?> clazz;
                try {
                    clazz = Class.forName(handlerMethodClass);
                } catch (ClassNotFoundException e) {
                    log.warn("Could not find class", e);
                    continue;
                }
                if (clazz != null) {
                    Annotation[] annotations = clazz.getAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof Controller) {
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
