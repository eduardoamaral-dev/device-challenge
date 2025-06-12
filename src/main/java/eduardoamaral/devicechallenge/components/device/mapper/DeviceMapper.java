package eduardoamaral.devicechallenge.components.device.mapper;

import eduardoamaral.devicechallenge.components.device.dto.DeviceRequestDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceResponseDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceState;
import eduardoamaral.devicechallenge.shared.persistence.model.DeviceEntity;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class DeviceMapper {

    public DeviceEntity toEntity(DeviceRequestDTO dto){
        return DeviceEntity.builder()
                .brand(dto.getBrand())
                .name(dto.getName())
                .state(stringToDeviceState(dto.getState()))
                .build();
    }

    public DeviceResponseDTO toResponse(DeviceEntity entity){
        return DeviceResponseDTO.builder()
                .brand(entity.getBrand())
                .name(entity.getName())
                .state(deviceStateToString(entity.getState()))
                .createdAt(dateToInstant(entity.getCreatedAt()))
                .id(entity.getId().toString())
                .build();
    }

    private Instant dateToInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

    public DeviceState stringToDeviceState(String state) {
        if (state == null) {
            return null;
        }
        String normalized = state.trim().toUpperCase().replace(" ", "_");
        try{
            return DeviceState.valueOf(normalized);
        }
        catch(Exception e){
            throw new ValidationException("Invalid device state: " + normalized);
        }
    }

    private String deviceStateToString(DeviceState state) {
        if (state == null) {
            return null;
        }
        return state.name().replace("_", " ");
    }

}
