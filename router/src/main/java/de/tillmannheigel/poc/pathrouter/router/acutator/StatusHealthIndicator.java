package de.tillmannheigel.poc.pathrouter.router.acutator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StatusHealthIndicator implements HealthIndicator {

    private static final Health UP = Health.status("up").build();
    private static final Health MAINTENANCE = Health.status("maintenance").build();
    private Health health = MAINTENANCE;

    @Override
    public Health health() {
        return health;
    }

    public void up(){
        health = UP;
    }

    public void maintenance(){
        health = MAINTENANCE;
    }
}
