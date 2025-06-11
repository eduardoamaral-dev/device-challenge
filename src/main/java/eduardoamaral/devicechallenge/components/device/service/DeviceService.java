package eduardoamaral.devicechallenge.components.device.service;

import eduardoamaral.devicechallenge.components.device.dto.DeviceQuery;
import eduardoamaral.devicechallenge.components.device.dto.DeviceRequestDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceService {
    DeviceResponseDTO create(DeviceRequestDTO device);
    DeviceResponseDTO update(DeviceRequestDTO device);
    List<DeviceResponseDTO> searchDevices(DeviceQuery query);
    Optional<DeviceResponseDTO> getDevice(String deviceId);
    List<DeviceResponseDTO> getAllDevices();
    void deleteDevice(String deviceId);
}
