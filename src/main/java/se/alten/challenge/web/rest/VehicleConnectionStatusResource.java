package se.alten.challenge.web.rest;
import se.alten.challenge.domain.VehicleConnectionStatus;
import se.alten.challenge.repository.VehicleConnectionStatusRepository;
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
 * REST controller for managing VehicleConnectionStatus.
 */
@RestController
@RequestMapping("/api")
public class VehicleConnectionStatusResource {

    private final Logger log = LoggerFactory.getLogger(VehicleConnectionStatusResource.class);

    private static final String ENTITY_NAME = "vdsVehicleConnectionStatus";

    private final VehicleConnectionStatusRepository vehicleConnectionStatusRepository;

    public VehicleConnectionStatusResource(VehicleConnectionStatusRepository vehicleConnectionStatusRepository) {
        this.vehicleConnectionStatusRepository = vehicleConnectionStatusRepository;
    }

    /**
     * POST  /vehicle-connection-statuses : Create a new vehicleConnectionStatus.
     *
     * @param vehicleConnectionStatus the vehicleConnectionStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vehicleConnectionStatus, or with status 400 (Bad Request) if the vehicleConnectionStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vehicle-connection-statuses")
    public ResponseEntity<VehicleConnectionStatus> createVehicleConnectionStatus(@Valid @RequestBody VehicleConnectionStatus vehicleConnectionStatus) throws URISyntaxException {
        log.debug("REST request to save VehicleConnectionStatus : {}", vehicleConnectionStatus);
        if (vehicleConnectionStatus.getId() != null) {
            throw new BadRequestAlertException("A new vehicleConnectionStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VehicleConnectionStatus result = vehicleConnectionStatusRepository.save(vehicleConnectionStatus);
        return ResponseEntity.created(new URI("/api/vehicle-connection-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vehicle-connection-statuses : Updates an existing vehicleConnectionStatus.
     *
     * @param vehicleConnectionStatus the vehicleConnectionStatus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vehicleConnectionStatus,
     * or with status 400 (Bad Request) if the vehicleConnectionStatus is not valid,
     * or with status 500 (Internal Server Error) if the vehicleConnectionStatus couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vehicle-connection-statuses")
    public ResponseEntity<VehicleConnectionStatus> updateVehicleConnectionStatus(@Valid @RequestBody VehicleConnectionStatus vehicleConnectionStatus) throws URISyntaxException {
        log.debug("REST request to update VehicleConnectionStatus : {}", vehicleConnectionStatus);
        if (vehicleConnectionStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VehicleConnectionStatus result = vehicleConnectionStatusRepository.save(vehicleConnectionStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vehicleConnectionStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vehicle-connection-statuses : get all the vehicleConnectionStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vehicleConnectionStatuses in body
     */
    @GetMapping("/vehicle-connection-statuses")
    public List<VehicleConnectionStatus> getAllVehicleConnectionStatuses() {
        log.debug("REST request to get all VehicleConnectionStatuses");
        return vehicleConnectionStatusRepository.findAll();
    }

    /**
     * GET  /vehicle-connection-statuses/:id : get the "id" vehicleConnectionStatus.
     *
     * @param id the id of the vehicleConnectionStatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vehicleConnectionStatus, or with status 404 (Not Found)
     */
    @GetMapping("/vehicle-connection-statuses/{id}")
    public ResponseEntity<VehicleConnectionStatus> getVehicleConnectionStatus(@PathVariable String id) {
        log.debug("REST request to get VehicleConnectionStatus : {}", id);
        Optional<VehicleConnectionStatus> vehicleConnectionStatus = vehicleConnectionStatusRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vehicleConnectionStatus);
    }

    /**
     * DELETE  /vehicle-connection-statuses/:id : delete the "id" vehicleConnectionStatus.
     *
     * @param id the id of the vehicleConnectionStatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vehicle-connection-statuses/{id}")
    public ResponseEntity<Void> deleteVehicleConnectionStatus(@PathVariable String id) {
        log.debug("REST request to delete VehicleConnectionStatus : {}", id);
        vehicleConnectionStatusRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
