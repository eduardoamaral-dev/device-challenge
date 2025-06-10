package eduardoamaral.devicechallenge.components.device.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceRequestDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String brand;
    @NotBlank
    private String state;
}
