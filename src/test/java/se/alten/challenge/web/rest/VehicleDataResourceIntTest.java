package se.alten.challenge.web.rest;

import se.alten.challenge.VdsApp;

import se.alten.challenge.domain.VehicleData;
import se.alten.challenge.repository.VehicleDataRepository;
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
 * Test class for the VehicleDataResource REST controller.
 *
 * @see VehicleDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VdsApp.class)
public class VehicleDataResourceIntTest {

    private static final String DEFAULT_VEHICLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final Double DEFAULT_SPEED = 1D;
    private static final Double UPDATED_SPEED = 2D;

    private static final Double DEFAULT_FUEL_LEVEL = 1D;
    private static final Double UPDATED_FUEL_LEVEL = 2D;

    private static final Instant DEFAULT_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private VehicleDataRepository vehicleDataRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restVehicleDataMockMvc;

    private VehicleData vehicleData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VehicleDataResource vehicleDataResource = new VehicleDataResource(vehicleDataRepository);
        this.restVehicleDataMockMvc = MockMvcBuilders.standaloneSetup(vehicleDataResource)
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
    public static VehicleData createEntity() {
        VehicleData vehicleData = new VehicleData()
            .vehicleId(DEFAULT_VEHICLE_ID)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .speed(DEFAULT_SPEED)
            .fuelLevel(DEFAULT_FUEL_LEVEL)
            .timeStamp(DEFAULT_TIME_STAMP);
        return vehicleData;
    }

    @Before
    public void initTest() {
        vehicleDataRepository.deleteAll();
        vehicleData = createEntity();
    }

    @Test
    public void createVehicleData() throws Exception {
        int databaseSizeBeforeCreate = vehicleDataRepository.findAll().size();

        // Create the VehicleData
        restVehicleDataMockMvc.perform(post("/api/vehicle-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleData)))
            .andExpect(status().isCreated());

        // Validate the VehicleData in the database
        List<VehicleData> vehicleDataList = vehicleDataRepository.findAll();
        assertThat(vehicleDataList).hasSize(databaseSizeBeforeCreate + 1);
        VehicleData testVehicleData = vehicleDataList.get(vehicleDataList.size() - 1);
        assertThat(testVehicleData.getVehicleId()).isEqualTo(DEFAULT_VEHICLE_ID);
        assertThat(testVehicleData.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testVehicleData.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testVehicleData.getSpeed()).isEqualTo(DEFAULT_SPEED);
        assertThat(testVehicleData.getFuelLevel()).isEqualTo(DEFAULT_FUEL_LEVEL);
        assertThat(testVehicleData.getTimeStamp()).isEqualTo(DEFAULT_TIME_STAMP);
    }

    @Test
    public void createVehicleDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vehicleDataRepository.findAll().size();

        // Create the VehicleData with an existing ID
        vehicleData.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleDataMockMvc.perform(post("/api/vehicle-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleData)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleData in the database
        List<VehicleData> vehicleDataList = vehicleDataRepository.findAll();
        assertThat(vehicleDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkVehicleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleDataRepository.findAll().size();
        // set the field null
        vehicleData.setVehicleId(null);

        // Create the VehicleData, which fails.

        restVehicleDataMockMvc.perform(post("/api/vehicle-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleData)))
            .andExpect(status().isBadRequest());

        List<VehicleData> vehicleDataList = vehicleDataRepository.findAll();
        assertThat(vehicleDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllVehicleData() throws Exception {
        // Initialize the database
        vehicleDataRepository.save(vehicleData);

        // Get all the vehicleDataList
        restVehicleDataMockMvc.perform(get("/api/vehicle-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleData.getId())))
            .andExpect(jsonPath("$.[*].vehicleId").value(hasItem(DEFAULT_VEHICLE_ID.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.toString())))
            .andExpect(jsonPath("$.[*].speed").value(hasItem(DEFAULT_SPEED.doubleValue())))
            .andExpect(jsonPath("$.[*].fuelLevel").value(hasItem(DEFAULT_FUEL_LEVEL.doubleValue())))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(DEFAULT_TIME_STAMP.toString())));
    }
    
    @Test
    public void getVehicleData() throws Exception {
        // Initialize the database
        vehicleDataRepository.save(vehicleData);

        // Get the vehicleData
        restVehicleDataMockMvc.perform(get("/api/vehicle-data/{id}", vehicleData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleData.getId()))
            .andExpect(jsonPath("$.vehicleId").value(DEFAULT_VEHICLE_ID.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.toString()))
            .andExpect(jsonPath("$.speed").value(DEFAULT_SPEED.doubleValue()))
            .andExpect(jsonPath("$.fuelLevel").value(DEFAULT_FUEL_LEVEL.doubleValue()))
            .andExpect(jsonPath("$.timeStamp").value(DEFAULT_TIME_STAMP.toString()));
    }

    @Test
    public void getNonExistingVehicleData() throws Exception {
        // Get the vehicleData
        restVehicleDataMockMvc.perform(get("/api/vehicle-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateVehicleData() throws Exception {
        // Initialize the database
        vehicleDataRepository.save(vehicleData);

        int databaseSizeBeforeUpdate = vehicleDataRepository.findAll().size();

        // Update the vehicleData
        VehicleData updatedVehicleData = vehicleDataRepository.findById(vehicleData.getId()).get();
        updatedVehicleData
            .vehicleId(UPDATED_VEHICLE_ID)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .speed(UPDATED_SPEED)
            .fuelLevel(UPDATED_FUEL_LEVEL)
            .timeStamp(UPDATED_TIME_STAMP);

        restVehicleDataMockMvc.perform(put("/api/vehicle-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVehicleData)))
            .andExpect(status().isOk());

        // Validate the VehicleData in the database
        List<VehicleData> vehicleDataList = vehicleDataRepository.findAll();
        assertThat(vehicleDataList).hasSize(databaseSizeBeforeUpdate);
        VehicleData testVehicleData = vehicleDataList.get(vehicleDataList.size() - 1);
        assertThat(testVehicleData.getVehicleId()).isEqualTo(UPDATED_VEHICLE_ID);
        assertThat(testVehicleData.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testVehicleData.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testVehicleData.getSpeed()).isEqualTo(UPDATED_SPEED);
        assertThat(testVehicleData.getFuelLevel()).isEqualTo(UPDATED_FUEL_LEVEL);
        assertThat(testVehicleData.getTimeStamp()).isEqualTo(UPDATED_TIME_STAMP);
    }

    @Test
    public void updateNonExistingVehicleData() throws Exception {
        int databaseSizeBeforeUpdate = vehicleDataRepository.findAll().size();

        // Create the VehicleData

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleDataMockMvc.perform(put("/api/vehicle-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleData)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleData in the database
        List<VehicleData> vehicleDataList = vehicleDataRepository.findAll();
        assertThat(vehicleDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteVehicleData() throws Exception {
        // Initialize the database
        vehicleDataRepository.save(vehicleData);

        int databaseSizeBeforeDelete = vehicleDataRepository.findAll().size();

        // Delete the vehicleData
        restVehicleDataMockMvc.perform(delete("/api/vehicle-data/{id}", vehicleData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VehicleData> vehicleDataList = vehicleDataRepository.findAll();
        assertThat(vehicleDataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleData.class);
        VehicleData vehicleData1 = new VehicleData();
        vehicleData1.setId("id1");
        VehicleData vehicleData2 = new VehicleData();
        vehicleData2.setId(vehicleData1.getId());
        assertThat(vehicleData1).isEqualTo(vehicleData2);
        vehicleData2.setId("id2");
        assertThat(vehicleData1).isNotEqualTo(vehicleData2);
        vehicleData1.setId(null);
        assertThat(vehicleData1).isNotEqualTo(vehicleData2);
    }
}
