package eduardoamaral.devicechallenge.components.device.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class DeviceResponseDTO {
    private UUID id;
    private String name;
    private String brand;
    private DeviceState state;
    private Instant createdAt;
}
