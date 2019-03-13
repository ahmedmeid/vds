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
 * A VehicleData.
 */
@Document(collection = "vehicle_data")
public class VehicleData implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @NotNull
    @Field("vehicle_id")
    private String vehicleId;

    @Field("longitude")
    private String longitude;

    @Field("latitude")
    private String latitude;

    @Field("speed")
    private Double speed;

    @Field("fuel_level")
    private Double fuelLevel;

    @Field("time_stamp")
    private Instant timeStamp;

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

    public VehicleData vehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
        return this;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getLongitude() {
        return longitude;
    }

    public VehicleData longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public VehicleData latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public VehicleData speed(Double speed) {
        this.speed = speed;
        return this;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getFuelLevel() {
        return fuelLevel;
    }

    public VehicleData fuelLevel(Double fuelLevel) {
        this.fuelLevel = fuelLevel;
        return this;
    }

    public void setFuelLevel(Double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public VehicleData timeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
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
        VehicleData vehicleData = (VehicleData) o;
        if (vehicleData.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vehicleData.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VehicleData{" +
            "id=" + getId() +
            ", vehicleId='" + getVehicleId() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", speed=" + getSpeed() +
            ", fuelLevel=" + getFuelLevel() +
            ", timeStamp='" + getTimeStamp() + "'" +
            "}";
    }
}
