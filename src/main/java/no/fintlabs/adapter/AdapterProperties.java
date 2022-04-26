package no.fintlabs.adapter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.fintlabs.adapter.models.AdapterCapability;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
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

    private Set<AdapterCapability> capabilities;

    public long getPingIntervalMs() {
        return Duration.parse("PT" + pingInterval + "M").toMillis();
    }
}
