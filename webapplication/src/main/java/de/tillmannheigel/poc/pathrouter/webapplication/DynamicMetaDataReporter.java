package de.tillmannheigel.poc.pathrouter.webapplication;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.web.mappings.MappingDescriptionProvider;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDescription;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDetails;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletsMappingDescriptionProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.ApplicationInfoManager;

@Component
public class DynamicMetaDataReporter {

    @Autowired
    ApplicationContext ctx;
    @Autowired
    Collection<MappingDescriptionProvider> descriptionProviders;
    @Autowired
    private ApplicationInfoManager aim;

    //@PostConstruct
    public void init() {
        List<String> mappingPatterns = new ArrayList<>();
        List<DispatcherServletMappingDescription> servletMappingDescriptions = getServletMappingDescriptions();
        for (DispatcherServletMappingDescription dispatcherServletMappingDescription : servletMappingDescriptions) {
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
                        if (annotation instanceof Controller
                                || annotation instanceof RestController) {
                            if (details.getRequestMappingConditions() != null
                                    && details.getRequestMappingConditions().getPatterns() != null) {
                                mappingPatterns.addAll(details.getRequestMappingConditions().getPatterns());

                            }
                        }
                    }
                }

            }
        }
        String str = String.join(",", mappingPatterns);
        Map<String, String> map = aim.getInfo().getMetadata();
        map.put("provided_paths", str);
    }

    private List<DispatcherServletMappingDescription> getServletMappingDescriptions() {
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