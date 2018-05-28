package de.tillmannheigel.poc.pathrouter.webapplication.routesReporter.rules;

import java.util.List;

import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDescription;

import de.tillmannheigel.poc.pathrouter.webapplication.routesReporter.Route;

public interface Rule {
    List<Route> applyRule(List<DispatcherServletMappingDescription> servletMappingDescriptions);
}
