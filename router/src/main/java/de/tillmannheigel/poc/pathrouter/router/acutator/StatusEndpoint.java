package de.tillmannheigel.poc.pathrouter.router.acutator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@Endpoint(id = "status")
@RestController
public class StatusEndpoint {

    @Autowired
    private StatusHealthIndicator statusHealthIndicator;

    @GetMapping(path = "/actuator/status/up", produces = "text/plain")
    public String setStatusUp(){
        statusHealthIndicator.up();
        return readStatus();
    }

    @GetMapping(path = "/actuator/status/maintenance", produces = "text/plain")
    public String setStatusMaintenance(){
        statusHealthIndicator.maintenance();
        return readStatus();
    }

    @ReadOperation(produces = "text/plain")
    public String readStatus(){
        return statusHealthIndicator.health().getStatus().toString();
    }

}
