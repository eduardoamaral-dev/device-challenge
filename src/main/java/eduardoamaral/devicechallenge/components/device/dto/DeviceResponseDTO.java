package eduardoamaral.devicechallenge.components.device.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class DeviceResponseDTO {
    private String id;
    private String name;
    private String brand;
    private String state;
    private Instant createdAt;
}
