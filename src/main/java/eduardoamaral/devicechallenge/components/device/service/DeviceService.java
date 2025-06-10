package eduardoamaral.devicechallenge.components.device.service;

import eduardoamaral.devicechallenge.components.device.dto.DeviceQuery;
import eduardoamaral.devicechallenge.components.device.dto.DeviceRequestDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceResponseDTO;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
    DeviceResponseDTO create(DeviceRequestDTO device);
    DeviceResponseDTO update(DeviceRequestDTO device);
    List<DeviceResponseDTO> searchDevices(DeviceQuery query);
    DeviceResponseDTO getDevice(UUID deviceId);
    List<DeviceResponseDTO> getAllDevices();
    boolean tryDeleteDevice(UUID deviceId);
}
