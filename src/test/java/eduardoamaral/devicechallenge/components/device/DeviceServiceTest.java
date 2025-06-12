package eduardoamaral.devicechallenge.components.device;


import com.fasterxml.jackson.databind.ObjectMapper;
import eduardoamaral.devicechallenge.components.device.dto.DeviceRequestDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceResponseDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
