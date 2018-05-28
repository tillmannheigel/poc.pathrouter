package de.tillmannheigel.poc.pathrouter.webapplication.routesReporter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Route {
    String pathPattern;
}
