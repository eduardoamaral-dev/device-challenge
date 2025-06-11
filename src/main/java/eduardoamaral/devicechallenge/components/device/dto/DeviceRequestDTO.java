package eduardoamaral.devicechallenge.components.device.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class DeviceRequestDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String brand;
    @NotBlank
    private String state;
}
