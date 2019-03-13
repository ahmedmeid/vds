package se.alten.challenge.web.rest;
import se.alten.challenge.domain.VehicleData;
import se.alten.challenge.repository.VehicleDataRepository;
import se.alten.challenge.web.rest.errors.BadRequestAlertException;
import se.alten.challenge.web.rest.util.HeaderUtil;
import se.alten.challenge.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VehicleData.
 */
@RestController
@RequestMapping("/api")
public class VehicleDataResource {

    private final Logger log = LoggerFactory.getLogger(VehicleDataResource.class);

    private static final String ENTITY_NAME = "vdsVehicleData";

    private final VehicleDataRepository vehicleDataRepository;

    public VehicleDataResource(VehicleDataRepository vehicleDataRepository) {
        this.vehicleDataRepository = vehicleDataRepository;
    }

    /**
     * POST  /vehicle-data : Create a new vehicleData.
     *
     * @param vehicleData the vehicleData to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vehicleData, or with status 400 (Bad Request) if the vehicleData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vehicle-data")
    public ResponseEntity<VehicleData> createVehicleData(@Valid @RequestBody VehicleData vehicleData) throws URISyntaxException {
        log.debug("REST request to save VehicleData : {}", vehicleData);
        if (vehicleData.getId() != null) {
            throw new BadRequestAlertException("A new vehicleData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VehicleData result = vehicleDataRepository.save(vehicleData);
        return ResponseEntity.created(new URI("/api/vehicle-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vehicle-data : Updates an existing vehicleData.
     *
     * @param vehicleData the vehicleData to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vehicleData,
     * or with status 400 (Bad Request) if the vehicleData is not valid,
     * or with status 500 (Internal Server Error) if the vehicleData couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vehicle-data")
    public ResponseEntity<VehicleData> updateVehicleData(@Valid @RequestBody VehicleData vehicleData) throws URISyntaxException {
        log.debug("REST request to update VehicleData : {}", vehicleData);
        if (vehicleData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VehicleData result = vehicleDataRepository.save(vehicleData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vehicleData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vehicle-data : get all the vehicleData.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of vehicleData in body
     */
    @GetMapping("/vehicle-data")
    public ResponseEntity<List<VehicleData>> getAllVehicleData(Pageable pageable) {
        log.debug("REST request to get a page of VehicleData");
        Page<VehicleData> page = vehicleDataRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vehicle-data");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /vehicle-data/:id : get the "id" vehicleData.
     *
     * @param id the id of the vehicleData to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vehicleData, or with status 404 (Not Found)
     */
    @GetMapping("/vehicle-data/{id}")
    public ResponseEntity<VehicleData> getVehicleData(@PathVariable String id) {
        log.debug("REST request to get VehicleData : {}", id);
        Optional<VehicleData> vehicleData = vehicleDataRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vehicleData);
    }

    /**
     * DELETE  /vehicle-data/:id : delete the "id" vehicleData.
     *
     * @param id the id of the vehicleData to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vehicle-data/{id}")
    public ResponseEntity<Void> deleteVehicleData(@PathVariable String id) {
        log.debug("REST request to delete VehicleData : {}", id);
        vehicleDataRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
