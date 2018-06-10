package de.tillmannheigel.poc.pathrouter.webapplication.routesReporter;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.appinfo.ApplicationInfoManager;

@Component
public class RoutesPublisher {

    public static final String PROVIDED_PATH_PREFIX = "provided_path_";
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private ApplicationInfoManager applicationInfoManager;

    public void publish(Set<Route> routesSet) throws JsonProcessingException {
        Map<String, String> map = applicationInfoManager.getInfo().getMetadata();
        final Iterator<Route> iterator = routesSet.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            Route route = iterator.next();
            map.put(PROVIDED_PATH_PREFIX + i, OBJECT_MAPPER.writeValueAsString(route));
        }
    }

}
