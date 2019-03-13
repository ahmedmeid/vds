package se.alten.challenge.web.rest;
import se.alten.challenge.domain.VehicleConnectionStatusHistory;
import se.alten.challenge.repository.VehicleConnectionStatusHistoryRepository;
import se.alten.challenge.web.rest.errors.BadRequestAlertException;
import se.alten.challenge.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VehicleConnectionStatusHistory.
 */
@RestController
@RequestMapping("/api")
public class VehicleConnectionStatusHistoryResource {

    private final Logger log = LoggerFactory.getLogger(VehicleConnectionStatusHistoryResource.class);

    private static final String ENTITY_NAME = "vdsVehicleConnectionStatusHistory";

    private final VehicleConnectionStatusHistoryRepository vehicleConnectionStatusHistoryRepository;

    public VehicleConnectionStatusHistoryResource(VehicleConnectionStatusHistoryRepository vehicleConnectionStatusHistoryRepository) {
        this.vehicleConnectionStatusHistoryRepository = vehicleConnectionStatusHistoryRepository;
    }

    /**
     * POST  /vehicle-connection-status-histories : Create a new vehicleConnectionStatusHistory.
     *
     * @param vehicleConnectionStatusHistory the vehicleConnectionStatusHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vehicleConnectionStatusHistory, or with status 400 (Bad Request) if the vehicleConnectionStatusHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vehicle-connection-status-histories")
    public ResponseEntity<VehicleConnectionStatusHistory> createVehicleConnectionStatusHistory(@Valid @RequestBody VehicleConnectionStatusHistory vehicleConnectionStatusHistory) throws URISyntaxException {
        log.debug("REST request to save VehicleConnectionStatusHistory : {}", vehicleConnectionStatusHistory);
        if (vehicleConnectionStatusHistory.getId() != null) {
            throw new BadRequestAlertException("A new vehicleConnectionStatusHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VehicleConnectionStatusHistory result = vehicleConnectionStatusHistoryRepository.save(vehicleConnectionStatusHistory);
        return ResponseEntity.created(new URI("/api/vehicle-connection-status-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vehicle-connection-status-histories : Updates an existing vehicleConnectionStatusHistory.
     *
     * @param vehicleConnectionStatusHistory the vehicleConnectionStatusHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vehicleConnectionStatusHistory,
     * or with status 400 (Bad Request) if the vehicleConnectionStatusHistory is not valid,
     * or with status 500 (Internal Server Error) if the vehicleConnectionStatusHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vehicle-connection-status-histories")
    public ResponseEntity<VehicleConnectionStatusHistory> updateVehicleConnectionStatusHistory(@Valid @RequestBody VehicleConnectionStatusHistory vehicleConnectionStatusHistory) throws URISyntaxException {
        log.debug("REST request to update VehicleConnectionStatusHistory : {}", vehicleConnectionStatusHistory);
        if (vehicleConnectionStatusHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VehicleConnectionStatusHistory result = vehicleConnectionStatusHistoryRepository.save(vehicleConnectionStatusHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vehicleConnectionStatusHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vehicle-connection-status-histories : get all the vehicleConnectionStatusHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vehicleConnectionStatusHistories in body
     */
    @GetMapping("/vehicle-connection-status-histories")
    public List<VehicleConnectionStatusHistory> getAllVehicleConnectionStatusHistories() {
        log.debug("REST request to get all VehicleConnectionStatusHistories");
        return vehicleConnectionStatusHistoryRepository.findAll();
    }

    /**
     * GET  /vehicle-connection-status-histories/:id : get the "id" vehicleConnectionStatusHistory.
     *
     * @param id the id of the vehicleConnectionStatusHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vehicleConnectionStatusHistory, or with status 404 (Not Found)
     */
    @GetMapping("/vehicle-connection-status-histories/{id}")
    public ResponseEntity<VehicleConnectionStatusHistory> getVehicleConnectionStatusHistory(@PathVariable String id) {
        log.debug("REST request to get VehicleConnectionStatusHistory : {}", id);
        Optional<VehicleConnectionStatusHistory> vehicleConnectionStatusHistory = vehicleConnectionStatusHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vehicleConnectionStatusHistory);
    }

    /**
     * DELETE  /vehicle-connection-status-histories/:id : delete the "id" vehicleConnectionStatusHistory.
     *
     * @param id the id of the vehicleConnectionStatusHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vehicle-connection-status-histories/{id}")
    public ResponseEntity<Void> deleteVehicleConnectionStatusHistory(@PathVariable String id) {
        log.debug("REST request to delete VehicleConnectionStatusHistory : {}", id);
        vehicleConnectionStatusHistoryRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
