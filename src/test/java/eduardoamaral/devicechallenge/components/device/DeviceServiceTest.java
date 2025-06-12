package eduardoamaral.devicechallenge.components.device;


import com.fasterxml.jackson.databind.ObjectMapper;
import eduardoamaral.devicechallenge.components.device.dto.DeviceRequestDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceResponseDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceState;
import eduardoamaral.devicechallenge.shared.persistence.model.DeviceEntity;
import eduardoamaral.devicechallenge.shared.persistence.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class DeviceServiceTest {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static DeviceRequestDTO validRequest;
    private static DeviceRequestDTO invalidRequest;

    @BeforeAll
    static void setup() {
        // Arrange
        validRequest = DeviceRequestDTO.builder()
                .brand("Samsung")
                .name("Galaxy")
                .state("in use")
                .build();
        invalidRequest = DeviceRequestDTO.builder()
                .brand("")
                .name(null)
                .state("This is not an state")
                .build();
    }

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("embeddedTestingDB")
            .withUsername("testingUser")
            .withPassword("123");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Test
    public void addDevice_should_save_and_return_device_when_request_is_valid() throws Exception {
        // Act
        MvcResult mvcResponse = mockMvc.perform(post("/devices/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.brand").value("Samsung"))
                .andExpect(jsonPath("$.name").value("Galaxy"))
                .andExpect(jsonPath("$.state").value("IN USE"))
                .andReturn();

        DeviceResponseDTO response = objectMapper.readValue(mvcResponse.getResponse().getContentAsString(), DeviceResponseDTO.class);
        var savedDevice = deviceRepository.findById(UUID.fromString(response.getId()));

        // Assert
        assertNotNull(response);
        assertEquals(validRequest.getBrand(), response.getBrand());
        assertEquals("IN USE", response.getState());
        assertEquals(validRequest.getName(), response.getName());
        assertTrue(savedDevice.isPresent());
    }

    @Test
    public void addDevice_should_not_save_device_when_request_is_invalid() throws Exception {
        // Act & assert
        mockMvc.perform(post("/devices/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    public void findById_should_return_the_device_with_ok_status_when_record_is_there() throws Exception {
        // Arrange
        var createdDeviceId = deviceRepository.save(DeviceEntity.builder()
                        .brand("Test brand")
                        .name("device for testing")
                        .state(DeviceState.AVAILABLE)
                        .build())
                .getId()
                .toString();

        // Act & assert
        String url = "/devices/find/" + createdDeviceId;
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdDeviceId))
                .andExpect(jsonPath("$.brand").value("Test brand"))
                .andExpect(jsonPath("$.name").value("device for testing"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andReturn();
    }

    @Test
    public void findById_should_return_not_found_status_when_device_is_not_there() throws Exception {
        // Act & assert
        String url = "/devices/find/4a63e1de-35b1-4ad8-83ff-1f7458ca6f62";
        mockMvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void findById_should_return_bad_request_status_when_uuid_is_invalid() throws Exception {
        // Act & assert
        String url = "/devices/find/thisIsNotAnUUID-123";
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void findByBrand_should_return_a_device_list_with_ok_status_when_they_are_present_in_db() throws Exception {
        // Arrange
        String brand = "github.com";
        deviceRepository.save(DeviceEntity.builder()
                        .brand(brand)
                        .name("github default")
                        .state(DeviceState.AVAILABLE)
                        .build());
        deviceRepository.save(DeviceEntity.builder()
                        .brand(brand)
                        .name("github pro")
                        .state(DeviceState.INACTIVE)
                        .build());
        deviceRepository.save(DeviceEntity.builder()
                        .brand(brand)
                        .name("github student pack")
                        .state(DeviceState.IN_USE)
                        .build());

        // Act
        String url = "/devices/find/brand/" + brand;
        var mvcResponse = mockMvc.perform(get(url))
                .andReturn()
                .getResponse();
        DeviceResponseDTO[] response = objectMapper.readValue(mvcResponse.getContentAsString(), DeviceResponseDTO[].class);

        // Assert
        assertEquals(3, response.length);
        assertEquals(brand, response[0].getBrand());
        assertEquals(brand, response[1].getBrand());
        assertEquals(brand, response[2].getBrand());
        assertEquals("github default", response[0].getName());
        assertEquals("github pro", response[1].getName());
        assertEquals("github student pack", response[2].getName());
    }

    @Test
    public void findByBrand_should_return_not_found_status_when_device_is_not_there() throws Exception {
        // Act & assert
        String url = "/devices/find/brand/mysteriousBrand";
        mockMvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void findByState_should_return_a_device_list_with_ok_status_when_they_are_present_in_db() throws Exception {
        // Arrange
        String brand = "testing brand";
        deviceRepository.deleteAll();
        deviceRepository.save(DeviceEntity.builder()
                        .brand(brand)
                        .name("laptop pro")
                        .state(DeviceState.AVAILABLE)
                        .build());

        deviceRepository.save(DeviceEntity.builder()
                        .brand(brand)
                        .name("laptop basic")
                        .state(DeviceState.IN_USE)
                        .build());

        deviceRepository.save(DeviceEntity.builder()
                        .brand(brand)
                        .name("laptop pro plus")
                        .state(DeviceState.IN_USE)
                        .build());

        // Act
        String url = "/devices/find/state/in_use";
        var mvcResponse = mockMvc.perform(get(url))
                .andReturn()
                .getResponse();
        DeviceResponseDTO[] response = objectMapper.readValue(mvcResponse.getContentAsString(), DeviceResponseDTO[].class);

        // Assert
        assertEquals(2, response.length);
        assertEquals("IN USE", response[0].getState());
        assertEquals("IN USE", response[1].getState());
        assertEquals("laptop basic", response[0].getName());
        assertEquals("laptop pro plus", response[1].getName());
    }

    @Test
    public void findByState_should_return_unprocessable_when_state_is_invalid() throws Exception {
        // Act & assert
        String url = "/devices/find/state/whatever";
        mockMvc.perform(get(url))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }


    @Test
    public void update_should_save_and_return_device_when_request_is_valid() throws Exception {
        // Arrange
        var deviceToBeUpdated = deviceRepository.save(DeviceEntity.builder()
                .brand("unknown brand")
                .name("unknown name")
                .state(DeviceState.AVAILABLE)
                .build());
        // Act
        MvcResult mvcResponse = mockMvc.perform(patch("/devices/update/" + deviceToBeUpdated.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.brand").value("Samsung"))
                .andExpect(jsonPath("$.name").value("Galaxy"))
                .andExpect(jsonPath("$.state").value("IN USE"))
                .andReturn();

        DeviceResponseDTO response = objectMapper.readValue(mvcResponse.getResponse().getContentAsString(), DeviceResponseDTO.class);
        var savedDevice = deviceRepository.findById(UUID.fromString(response.getId()));

        // Assert
        assertNotNull(response);
        assertEquals(validRequest.getBrand(), response.getBrand());
        assertEquals("IN USE", response.getState());
        assertEquals(validRequest.getName(), response.getName());
        assertTrue(savedDevice.isPresent());
    }

    @Test
    public void update_should_return_not_found_status_when_record_is_not_there() throws Exception {
        // Arrange
        var randomId = UUID.randomUUID().toString();

        // Act & Assert
        mockMvc.perform(patch("/devices/update/" + randomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound())
                .andReturn();
        var savedDevice = deviceRepository.findById(UUID.fromString(randomId));

        // Assert
        assertTrue(savedDevice.isEmpty());
    }

    @Test
    public void update_should_not_update_and_return_bad_request_when_device_is_in_use() throws Exception {
        // Arrange
        var deviceToBeUpdated = deviceRepository.save(DeviceEntity.builder()
                .brand("laptop brand")
                .name("laptop pro max")
                .state(DeviceState.IN_USE)
                .build());
        // Act
        mockMvc.perform(patch("/devices/update/" + deviceToBeUpdated.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());

        var savedDevice = deviceRepository.findById(deviceToBeUpdated.getId()).get();

        // Assert (that it is exactly as it was created)
        assertEquals(deviceToBeUpdated.getBrand(), savedDevice.getBrand());
        assertEquals(DeviceState.IN_USE, savedDevice.getState());
        assertEquals(deviceToBeUpdated.getName(), savedDevice.getName());
    }

    @Test
    public void delete_should_remove_and_return_ok_when_record_is_there_and_not_in_use() throws Exception {
        // Arrange
        var deviceToBeDeleted = deviceRepository.save(DeviceEntity.builder()
                .brand("laptop brand")
                .name("laptop pro max")
                .state(DeviceState.INACTIVE)
                .build());
        // Act
        mockMvc.perform(delete("/devices/remove/" + deviceToBeDeleted.getId().toString())
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk());

        var device = deviceRepository.findById(deviceToBeDeleted.getId());

        // Assert
        assertTrue(device.isEmpty());
    }

    @Test
    public void delete_should_NOT_remove_and_return_bad_request_when_record_is_in_use() throws Exception {
        // Arrange
        var deviceToBeDeleted = deviceRepository.save(DeviceEntity.builder()
                .brand("laptop brand")
                .name("laptop pro max")
                .state(DeviceState.IN_USE)
                .build());
        // Act
        mockMvc.perform(delete("/devices/remove/" + deviceToBeDeleted.getId().toString())
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());

        var device = deviceRepository.findById(deviceToBeDeleted.getId());

        // Assert
        assertTrue(device.isPresent());
    }

    @Test
    public void delete_should_return_not_found_when_record_is_not_there() throws Exception {
        // Arrange
        var randomId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(delete("/devices/remove/" + randomId.toString())
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAll_should_return_a_device_list_with_ok_status_when_they_are_present_in_db() throws Exception {
        // Arrange
        String brand = "testing brand";
        deviceRepository.deleteAll();
        deviceRepository.save(DeviceEntity.builder()
                .brand(brand)
                .name("laptop pro")
                .state(DeviceState.AVAILABLE)
                .build());

        deviceRepository.save(DeviceEntity.builder()
                .brand(brand)
                .name("laptop basic")
                .state(DeviceState.IN_USE)
                .build());

        deviceRepository.save(DeviceEntity.builder()
                .brand(brand)
                .name("laptop pro plus")
                .state(DeviceState.IN_USE)
                .build());

        // Act
        String url = "/devices/getAll";
        var mvcResponse = mockMvc.perform(get(url))
                .andReturn()
                .getResponse();
        DeviceResponseDTO[] response = objectMapper.readValue(mvcResponse.getContentAsString(), DeviceResponseDTO[].class);

        // Assert
        assertEquals(3, response.length);
    }

    @Test
    public void getAll_should_return_not_found_when_there_are_no_records_in_db() throws Exception {
        // Arrange
        deviceRepository.deleteAll();

        // Act & Assert
        String url = "/devices/getAll";
        mockMvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();
    }



}
