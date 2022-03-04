package no.fintlabs.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AdapterPing {
    private String adapterId;
    private String username;
    private String orgId;
//    @NotNull
//    private Status status;
    private long time;

//    public enum Status {
//        APPLICATION_HEALTHY,
//        APPLICATION_UNHEALTHY
//    }
}
