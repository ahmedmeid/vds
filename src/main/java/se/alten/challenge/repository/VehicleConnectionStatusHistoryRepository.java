package se.alten.challenge.repository;

import se.alten.challenge.domain.VehicleConnectionStatusHistory;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the VehicleConnectionStatusHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleConnectionStatusHistoryRepository extends MongoRepository<VehicleConnectionStatusHistory, String> {

}
