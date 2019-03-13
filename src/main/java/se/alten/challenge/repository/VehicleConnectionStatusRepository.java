package se.alten.challenge.repository;

import se.alten.challenge.domain.VehicleConnectionStatus;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the VehicleConnectionStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleConnectionStatusRepository extends MongoRepository<VehicleConnectionStatus, String> {

}
