package eduardoamaral.devicechallenge.components.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Device request payload")
public class DeviceRequestDTO {
    @NotBlank
    @Schema(
            description = "Name",
            example = "Laptop pro max",
            defaultValue = "Laptop pro max"
    )
    private String name;
    @Schema(
            description = "Brand",
            example = "Computers dummy company",
            defaultValue = "Computers dummy company"
    )
    @NotBlank
    private String brand;
    @Schema(
            description = "State",
            example = "inactive, in use, available",
            defaultValue = "available"
    )
    @NotBlank
    private String state;
}
