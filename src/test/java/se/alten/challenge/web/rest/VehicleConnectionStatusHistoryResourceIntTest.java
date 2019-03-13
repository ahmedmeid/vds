package se.alten.challenge.web.rest;

import se.alten.challenge.VdsApp;

import se.alten.challenge.domain.VehicleConnectionStatusHistory;
import se.alten.challenge.repository.VehicleConnectionStatusHistoryRepository;
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

/**
 * Test class for the VehicleConnectionStatusHistoryResource REST controller.
 *
 * @see VehicleConnectionStatusHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VdsApp.class)
public class VehicleConnectionStatusHistoryResourceIntTest {

    private static final String DEFAULT_VEHICLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_STATUS_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STATUS_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private VehicleConnectionStatusHistoryRepository vehicleConnectionStatusHistoryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restVehicleConnectionStatusHistoryMockMvc;

    private VehicleConnectionStatusHistory vehicleConnectionStatusHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VehicleConnectionStatusHistoryResource vehicleConnectionStatusHistoryResource = new VehicleConnectionStatusHistoryResource(vehicleConnectionStatusHistoryRepository);
        this.restVehicleConnectionStatusHistoryMockMvc = MockMvcBuilders.standaloneSetup(vehicleConnectionStatusHistoryResource)
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
    public static VehicleConnectionStatusHistory createEntity() {
        VehicleConnectionStatusHistory vehicleConnectionStatusHistory = new VehicleConnectionStatusHistory()
            .vehicleId(DEFAULT_VEHICLE_ID)
            .status(DEFAULT_STATUS)
            .statusAt(DEFAULT_STATUS_AT);
        return vehicleConnectionStatusHistory;
    }

    @Before
    public void initTest() {
        vehicleConnectionStatusHistoryRepository.deleteAll();
        vehicleConnectionStatusHistory = createEntity();
    }

    @Test
    public void createVehicleConnectionStatusHistory() throws Exception {
        int databaseSizeBeforeCreate = vehicleConnectionStatusHistoryRepository.findAll().size();

        // Create the VehicleConnectionStatusHistory
        restVehicleConnectionStatusHistoryMockMvc.perform(post("/api/vehicle-connection-status-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleConnectionStatusHistory)))
            .andExpect(status().isCreated());

        // Validate the VehicleConnectionStatusHistory in the database
        List<VehicleConnectionStatusHistory> vehicleConnectionStatusHistoryList = vehicleConnectionStatusHistoryRepository.findAll();
        assertThat(vehicleConnectionStatusHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        VehicleConnectionStatusHistory testVehicleConnectionStatusHistory = vehicleConnectionStatusHistoryList.get(vehicleConnectionStatusHistoryList.size() - 1);
        assertThat(testVehicleConnectionStatusHistory.getVehicleId()).isEqualTo(DEFAULT_VEHICLE_ID);
        assertThat(testVehicleConnectionStatusHistory.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testVehicleConnectionStatusHistory.getStatusAt()).isEqualTo(DEFAULT_STATUS_AT);
    }

    @Test
    public void createVehicleConnectionStatusHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vehicleConnectionStatusHistoryRepository.findAll().size();

        // Create the VehicleConnectionStatusHistory with an existing ID
        vehicleConnectionStatusHistory.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleConnectionStatusHistoryMockMvc.perform(post("/api/vehicle-connection-status-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleConnectionStatusHistory)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleConnectionStatusHistory in the database
        List<VehicleConnectionStatusHistory> vehicleConnectionStatusHistoryList = vehicleConnectionStatusHistoryRepository.findAll();
        assertThat(vehicleConnectionStatusHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkVehicleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleConnectionStatusHistoryRepository.findAll().size();
        // set the field null
        vehicleConnectionStatusHistory.setVehicleId(null);

        // Create the VehicleConnectionStatusHistory, which fails.

        restVehicleConnectionStatusHistoryMockMvc.perform(post("/api/vehicle-connection-status-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleConnectionStatusHistory)))
            .andExpect(status().isBadRequest());

        List<VehicleConnectionStatusHistory> vehicleConnectionStatusHistoryList = vehicleConnectionStatusHistoryRepository.findAll();
        assertThat(vehicleConnectionStatusHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllVehicleConnectionStatusHistories() throws Exception {
        // Initialize the database
        vehicleConnectionStatusHistoryRepository.save(vehicleConnectionStatusHistory);

        // Get all the vehicleConnectionStatusHistoryList
        restVehicleConnectionStatusHistoryMockMvc.perform(get("/api/vehicle-connection-status-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleConnectionStatusHistory.getId())))
            .andExpect(jsonPath("$.[*].vehicleId").value(hasItem(DEFAULT_VEHICLE_ID.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].statusAt").value(hasItem(DEFAULT_STATUS_AT.toString())));
    }
    
    @Test
    public void getVehicleConnectionStatusHistory() throws Exception {
        // Initialize the database
        vehicleConnectionStatusHistoryRepository.save(vehicleConnectionStatusHistory);

        // Get the vehicleConnectionStatusHistory
        restVehicleConnectionStatusHistoryMockMvc.perform(get("/api/vehicle-connection-status-histories/{id}", vehicleConnectionStatusHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleConnectionStatusHistory.getId()))
            .andExpect(jsonPath("$.vehicleId").value(DEFAULT_VEHICLE_ID.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.statusAt").value(DEFAULT_STATUS_AT.toString()));
    }

    @Test
    public void getNonExistingVehicleConnectionStatusHistory() throws Exception {
        // Get the vehicleConnectionStatusHistory
        restVehicleConnectionStatusHistoryMockMvc.perform(get("/api/vehicle-connection-status-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateVehicleConnectionStatusHistory() throws Exception {
        // Initialize the database
        vehicleConnectionStatusHistoryRepository.save(vehicleConnectionStatusHistory);

        int databaseSizeBeforeUpdate = vehicleConnectionStatusHistoryRepository.findAll().size();

        // Update the vehicleConnectionStatusHistory
        VehicleConnectionStatusHistory updatedVehicleConnectionStatusHistory = vehicleConnectionStatusHistoryRepository.findById(vehicleConnectionStatusHistory.getId()).get();
        updatedVehicleConnectionStatusHistory
            .vehicleId(UPDATED_VEHICLE_ID)
            .status(UPDATED_STATUS)
            .statusAt(UPDATED_STATUS_AT);

        restVehicleConnectionStatusHistoryMockMvc.perform(put("/api/vehicle-connection-status-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVehicleConnectionStatusHistory)))
            .andExpect(status().isOk());

        // Validate the VehicleConnectionStatusHistory in the database
        List<VehicleConnectionStatusHistory> vehicleConnectionStatusHistoryList = vehicleConnectionStatusHistoryRepository.findAll();
        assertThat(vehicleConnectionStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
        VehicleConnectionStatusHistory testVehicleConnectionStatusHistory = vehicleConnectionStatusHistoryList.get(vehicleConnectionStatusHistoryList.size() - 1);
        assertThat(testVehicleConnectionStatusHistory.getVehicleId()).isEqualTo(UPDATED_VEHICLE_ID);
        assertThat(testVehicleConnectionStatusHistory.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVehicleConnectionStatusHistory.getStatusAt()).isEqualTo(UPDATED_STATUS_AT);
    }

    @Test
    public void updateNonExistingVehicleConnectionStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = vehicleConnectionStatusHistoryRepository.findAll().size();

        // Create the VehicleConnectionStatusHistory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleConnectionStatusHistoryMockMvc.perform(put("/api/vehicle-connection-status-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleConnectionStatusHistory)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleConnectionStatusHistory in the database
        List<VehicleConnectionStatusHistory> vehicleConnectionStatusHistoryList = vehicleConnectionStatusHistoryRepository.findAll();
        assertThat(vehicleConnectionStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteVehicleConnectionStatusHistory() throws Exception {
        // Initialize the database
        vehicleConnectionStatusHistoryRepository.save(vehicleConnectionStatusHistory);

        int databaseSizeBeforeDelete = vehicleConnectionStatusHistoryRepository.findAll().size();

        // Delete the vehicleConnectionStatusHistory
        restVehicleConnectionStatusHistoryMockMvc.perform(delete("/api/vehicle-connection-status-histories/{id}", vehicleConnectionStatusHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VehicleConnectionStatusHistory> vehicleConnectionStatusHistoryList = vehicleConnectionStatusHistoryRepository.findAll();
        assertThat(vehicleConnectionStatusHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleConnectionStatusHistory.class);
        VehicleConnectionStatusHistory vehicleConnectionStatusHistory1 = new VehicleConnectionStatusHistory();
        vehicleConnectionStatusHistory1.setId("id1");
        VehicleConnectionStatusHistory vehicleConnectionStatusHistory2 = new VehicleConnectionStatusHistory();
        vehicleConnectionStatusHistory2.setId(vehicleConnectionStatusHistory1.getId());
        assertThat(vehicleConnectionStatusHistory1).isEqualTo(vehicleConnectionStatusHistory2);
        vehicleConnectionStatusHistory2.setId("id2");
        assertThat(vehicleConnectionStatusHistory1).isNotEqualTo(vehicleConnectionStatusHistory2);
        vehicleConnectionStatusHistory1.setId(null);
        assertThat(vehicleConnectionStatusHistory1).isNotEqualTo(vehicleConnectionStatusHistory2);
    }
}
