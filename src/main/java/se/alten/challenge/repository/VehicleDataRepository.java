package se.alten.challenge.repository;

import se.alten.challenge.domain.VehicleData;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the VehicleData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleDataRepository extends MongoRepository<VehicleData, String> {

}
