package de.tillmannheigel.poc.pathrouter.webapplication.routesReporter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.web.mappings.MappingDescriptionProvider;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDescription;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletsMappingDescriptionProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RequestMappingCollector {

    @Autowired
    ApplicationContext ctx;
    @Autowired
    Collection<MappingDescriptionProvider> descriptionProviders;

    public List<DispatcherServletMappingDescription> getRequestMappingDescriptions() {
        List<DispatcherServletMappingDescription> mappings = new ArrayList<>();
        descriptionProviders.stream()
                .filter(provider -> provider instanceof DispatcherServletsMappingDescriptionProvider)
                .map(provider -> (DispatcherServletsMappingDescriptionProvider) provider)
                .forEach(provider -> provider.describeMappings(ctx).values().stream().forEach(dispatcherServletMappingDescriptions -> {
                    mappings.addAll(dispatcherServletMappingDescriptions);
                }));
        return mappings;
    }


}
