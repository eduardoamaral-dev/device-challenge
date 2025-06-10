package eduardoamaral.devicechallenge.components.device.service;

import eduardoamaral.devicechallenge.components.device.dto.DeviceQuery;
import eduardoamaral.devicechallenge.components.device.domain.models.Device;

import java.util.UUID;

public interface DeviceService {
    Device create(Device device);
    Device update(Device device);
    Device searchDevices(DeviceQuery query);
    Device getDevice(UUID deviceId);
    Device getAllDevices();
}
