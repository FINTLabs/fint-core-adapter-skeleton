package no.fintlabs.adapter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.fintlabs.adapter.models.AdapterCapability;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "fint.adapter")
public class AdapterProperties {

    private int pingInterval;
    private String id;
    private String username;
    private String password;
    private String registrationId;
    private String baseUrl;
    private String orgId;

    private Map<String, AdapterCapability> capabilities;

    public Set<AdapterCapability> adapterCapabilityToSet() {
        return new HashSet<>(capabilities.values());
    }
    public long getPingIntervalMs() {
        return Duration.parse("PT" + pingInterval + "M").toMillis();
    }

    public long getFullSyncIntervalMs(String entity) {
        return Duration.parse("PT" + capabilities.get(entity).getFullSyncIntervalInDays() + "H").toMillis();
    }

    public AdapterCapability getCapabilityByResource(String resource) {
        return capabilities.get(resource);
    }
}
