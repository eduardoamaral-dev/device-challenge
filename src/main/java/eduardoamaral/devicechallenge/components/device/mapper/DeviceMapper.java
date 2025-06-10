package eduardoamaral.devicechallenge.components.device.mapper;

import eduardoamaral.devicechallenge.components.device.dto.DeviceRequestDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceResponseDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceState;
import eduardoamaral.devicechallenge.shared.persistence.model.DeviceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper
public interface DeviceMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "state", source = "state", qualifiedByName = "stringToDeviceState")
    DeviceEntity toEntity(DeviceRequestDTO dto);

    @Mapping(target = "id", qualifiedByName = "uuidToString")
    @Mapping(target = "state", qualifiedByName = "deviceStateToString")
    DeviceResponseDTO toResponse(DeviceEntity entity);

    default Instant map(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

    @Named("uuidToString")
    default String uuidToString(UUID id){
        return id.toString();
    }

    @Named("stringToDeviceState")
    default DeviceState stringToDeviceState(String state) {
        if (state == null) {
            return null;
        }
        String normalized = state.trim().toUpperCase().replace(" ", "_");
        return DeviceState.valueOf(normalized);
    }

    @Named("deviceStateToString")
    default String deviceStateToString(DeviceState state) {
        if (state == null) {
            return null;
        }
        return state.name().replace("_", " ");
    }

}
