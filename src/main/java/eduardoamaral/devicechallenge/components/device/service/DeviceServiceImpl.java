package eduardoamaral.devicechallenge.components.device.service;

import eduardoamaral.devicechallenge.components.device.dto.DeviceQuery;
import eduardoamaral.devicechallenge.components.device.domain.models.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    public Device create(Device device) {
        return null;
    }


    public Device update(Device device) {
        return null;
    }


    public Device searchDevices(DeviceQuery query) {
        return null;
    }


    public Device getDevice(UUID deviceId) {
        return null;
    }


    public Device getAllDevices() {
        return null;
    }
}
