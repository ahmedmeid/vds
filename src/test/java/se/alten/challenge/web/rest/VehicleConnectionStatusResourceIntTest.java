package se.alten.challenge.web.rest;

import se.alten.challenge.VdsApp;

import se.alten.challenge.domain.VehicleConnectionStatus;
import se.alten.challenge.repository.VehicleConnectionStatusRepository;
import se.alten.challenge.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static se.alten.challenge.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import se.alten.challenge.domain.enumeration.ConnectionStatus;
/**
 * Test class for the VehicleConnectionStatusResource REST controller.
 *
 * @see VehicleConnectionStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VdsApp.class)
public class VehicleConnectionStatusResourceIntTest {

    private static final String DEFAULT_VEHICLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_ID = "BBBBBBBBBB";

    private static final ConnectionStatus DEFAULT_STATUS = ConnectionStatus.CONNECTED;
    private static final ConnectionStatus UPDATED_STATUS = ConnectionStatus.DISCONNECTED;

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private VehicleConnectionStatusRepository vehicleConnectionStatusRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restVehicleConnectionStatusMockMvc;

    private VehicleConnectionStatus vehicleConnectionStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VehicleConnectionStatusResource vehicleConnectionStatusResource = new VehicleConnectionStatusResource(vehicleConnectionStatusRepository);
        this.restVehicleConnectionStatusMockMvc = MockMvcBuilders.standaloneSetup(vehicleConnectionStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleConnectionStatus createEntity() {
        VehicleConnectionStatus vehicleConnectionStatus = new VehicleConnectionStatus()
            .vehicleId(DEFAULT_VEHICLE_ID)
            .status(DEFAULT_STATUS)
            .lastUpdated(DEFAULT_LAST_UPDATED);
        return vehicleConnectionStatus;
    }

    @Before
    public void initTest() {
        vehicleConnectionStatusRepository.deleteAll();
        vehicleConnectionStatus = createEntity();
    }

    @Test
    public void createVehicleConnectionStatus() throws Exception {
        int databaseSizeBeforeCreate = vehicleConnectionStatusRepository.findAll().size();

        // Create the VehicleConnectionStatus
        restVehicleConnectionStatusMockMvc.perform(post("/api/vehicle-connection-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleConnectionStatus)))
            .andExpect(status().isCreated());

        // Validate the VehicleConnectionStatus in the database
        List<VehicleConnectionStatus> vehicleConnectionStatusList = vehicleConnectionStatusRepository.findAll();
        assertThat(vehicleConnectionStatusList).hasSize(databaseSizeBeforeCreate + 1);
        VehicleConnectionStatus testVehicleConnectionStatus = vehicleConnectionStatusList.get(vehicleConnectionStatusList.size() - 1);
        assertThat(testVehicleConnectionStatus.getVehicleId()).isEqualTo(DEFAULT_VEHICLE_ID);
        assertThat(testVehicleConnectionStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testVehicleConnectionStatus.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    public void createVehicleConnectionStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vehicleConnectionStatusRepository.findAll().size();

        // Create the VehicleConnectionStatus with an existing ID
        vehicleConnectionStatus.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleConnectionStatusMockMvc.perform(post("/api/vehicle-connection-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleConnectionStatus)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleConnectionStatus in the database
        List<VehicleConnectionStatus> vehicleConnectionStatusList = vehicleConnectionStatusRepository.findAll();
        assertThat(vehicleConnectionStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkVehicleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleConnectionStatusRepository.findAll().size();
        // set the field null
        vehicleConnectionStatus.setVehicleId(null);

        // Create the VehicleConnectionStatus, which fails.

        restVehicleConnectionStatusMockMvc.perform(post("/api/vehicle-connection-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleConnectionStatus)))
            .andExpect(status().isBadRequest());

        List<VehicleConnectionStatus> vehicleConnectionStatusList = vehicleConnectionStatusRepository.findAll();
        assertThat(vehicleConnectionStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllVehicleConnectionStatuses() throws Exception {
        // Initialize the database
        vehicleConnectionStatusRepository.save(vehicleConnectionStatus);

        // Get all the vehicleConnectionStatusList
        restVehicleConnectionStatusMockMvc.perform(get("/api/vehicle-connection-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleConnectionStatus.getId())))
            .andExpect(jsonPath("$.[*].vehicleId").value(hasItem(DEFAULT_VEHICLE_ID.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }
    
    @Test
    public void getVehicleConnectionStatus() throws Exception {
        // Initialize the database
        vehicleConnectionStatusRepository.save(vehicleConnectionStatus);

        // Get the vehicleConnectionStatus
        restVehicleConnectionStatusMockMvc.perform(get("/api/vehicle-connection-statuses/{id}", vehicleConnectionStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleConnectionStatus.getId()))
            .andExpect(jsonPath("$.vehicleId").value(DEFAULT_VEHICLE_ID.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    public void getNonExistingVehicleConnectionStatus() throws Exception {
        // Get the vehicleConnectionStatus
        restVehicleConnectionStatusMockMvc.perform(get("/api/vehicle-connection-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateVehicleConnectionStatus() throws Exception {
        // Initialize the database
        vehicleConnectionStatusRepository.save(vehicleConnectionStatus);

        int databaseSizeBeforeUpdate = vehicleConnectionStatusRepository.findAll().size();

        // Update the vehicleConnectionStatus
        VehicleConnectionStatus updatedVehicleConnectionStatus = vehicleConnectionStatusRepository.findById(vehicleConnectionStatus.getId()).get();
        updatedVehicleConnectionStatus
            .vehicleId(UPDATED_VEHICLE_ID)
            .status(UPDATED_STATUS)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restVehicleConnectionStatusMockMvc.perform(put("/api/vehicle-connection-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVehicleConnectionStatus)))
            .andExpect(status().isOk());

        // Validate the VehicleConnectionStatus in the database
        List<VehicleConnectionStatus> vehicleConnectionStatusList = vehicleConnectionStatusRepository.findAll();
        assertThat(vehicleConnectionStatusList).hasSize(databaseSizeBeforeUpdate);
        VehicleConnectionStatus testVehicleConnectionStatus = vehicleConnectionStatusList.get(vehicleConnectionStatusList.size() - 1);
        assertThat(testVehicleConnectionStatus.getVehicleId()).isEqualTo(UPDATED_VEHICLE_ID);
        assertThat(testVehicleConnectionStatus.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVehicleConnectionStatus.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    public void updateNonExistingVehicleConnectionStatus() throws Exception {
        int databaseSizeBeforeUpdate = vehicleConnectionStatusRepository.findAll().size();

        // Create the VehicleConnectionStatus

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleConnectionStatusMockMvc.perform(put("/api/vehicle-connection-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleConnectionStatus)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleConnectionStatus in the database
        List<VehicleConnectionStatus> vehicleConnectionStatusList = vehicleConnectionStatusRepository.findAll();
        assertThat(vehicleConnectionStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteVehicleConnectionStatus() throws Exception {
        // Initialize the database
        vehicleConnectionStatusRepository.save(vehicleConnectionStatus);

        int databaseSizeBeforeDelete = vehicleConnectionStatusRepository.findAll().size();

        // Delete the vehicleConnectionStatus
        restVehicleConnectionStatusMockMvc.perform(delete("/api/vehicle-connection-statuses/{id}", vehicleConnectionStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VehicleConnectionStatus> vehicleConnectionStatusList = vehicleConnectionStatusRepository.findAll();
        assertThat(vehicleConnectionStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleConnectionStatus.class);
        VehicleConnectionStatus vehicleConnectionStatus1 = new VehicleConnectionStatus();
        vehicleConnectionStatus1.setId("id1");
        VehicleConnectionStatus vehicleConnectionStatus2 = new VehicleConnectionStatus();
        vehicleConnectionStatus2.setId(vehicleConnectionStatus1.getId());
        assertThat(vehicleConnectionStatus1).isEqualTo(vehicleConnectionStatus2);
        vehicleConnectionStatus2.setId("id2");
        assertThat(vehicleConnectionStatus1).isNotEqualTo(vehicleConnectionStatus2);
        vehicleConnectionStatus1.setId(null);
        assertThat(vehicleConnectionStatus1).isNotEqualTo(vehicleConnectionStatus2);
    }
}
