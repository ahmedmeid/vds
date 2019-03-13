package se.alten.challenge.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import se.alten.challenge.domain.enumeration.ConnectionStatus;

/**
 * A VehicleConnectionStatus.
 */
@Document(collection = "vehicle_connection_status")
public class VehicleConnectionStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @NotNull
    @Field("vehicle_id")
    private String vehicleId;

    @Field("status")
    private ConnectionStatus status;

    @Field("last_updated")
    private Instant lastUpdated;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public VehicleConnectionStatus vehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
        return this;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public VehicleConnectionStatus status(ConnectionStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public VehicleConnectionStatus lastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VehicleConnectionStatus vehicleConnectionStatus = (VehicleConnectionStatus) o;
        if (vehicleConnectionStatus.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vehicleConnectionStatus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VehicleConnectionStatus{" +
            "id=" + getId() +
            ", vehicleId='" + getVehicleId() + "'" +
            ", status='" + getStatus() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
