package se.alten.challenge.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A VehicleConnectionStatusHistory.
 */
@Document(collection = "vehicle_connection_status_history")
public class VehicleConnectionStatusHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @NotNull
    @Field("vehicle_id")
    private String vehicleId;

    @Field("status")
    private String status;

    @Field("status_at")
    private Instant statusAt;

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

    public VehicleConnectionStatusHistory vehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
        return this;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getStatus() {
        return status;
    }

    public VehicleConnectionStatusHistory status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getStatusAt() {
        return statusAt;
    }

    public VehicleConnectionStatusHistory statusAt(Instant statusAt) {
        this.statusAt = statusAt;
        return this;
    }

    public void setStatusAt(Instant statusAt) {
        this.statusAt = statusAt;
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
        VehicleConnectionStatusHistory vehicleConnectionStatusHistory = (VehicleConnectionStatusHistory) o;
        if (vehicleConnectionStatusHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vehicleConnectionStatusHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VehicleConnectionStatusHistory{" +
            "id=" + getId() +
            ", vehicleId='" + getVehicleId() + "'" +
            ", status='" + getStatus() + "'" +
            ", statusAt='" + getStatusAt() + "'" +
            "}";
    }
}
