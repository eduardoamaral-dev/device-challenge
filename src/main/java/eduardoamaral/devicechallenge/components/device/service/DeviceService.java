package eduardoamaral.devicechallenge.components.device.service;

import eduardoamaral.devicechallenge.components.device.dto.DeviceQuery;
import eduardoamaral.devicechallenge.components.device.dto.DeviceRequestDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceService {
    DeviceResponseDTO create(DeviceRequestDTO device);
    DeviceResponseDTO update(String id, DeviceRequestDTO request);
    List<DeviceResponseDTO> searchDevices(DeviceQuery query);
    List<DeviceResponseDTO> getByBrand(String brand);
    List<DeviceResponseDTO> getByName(String name);
    Optional<DeviceResponseDTO> getById(String deviceId);
    List<DeviceResponseDTO> getAllDevices();
    void deleteDevice(String deviceId);
}
