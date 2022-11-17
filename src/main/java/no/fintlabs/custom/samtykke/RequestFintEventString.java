package no.fintlabs.custom.samtykke;

import lombok.*;
import no.fintlabs.adapter.models.RequestFintEventBase;

import java.io.Serializable;

/**
 * Represents a request to the adapter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class RequestFintEventString extends RequestFintEventBase {

    /**
     * The object to which the event applies
     */
    private String value;
}
