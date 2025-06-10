package eduardoamaral.devicechallenge.components.device.service;

import eduardoamaral.devicechallenge.components.device.dto.DeviceQuery;
import eduardoamaral.devicechallenge.components.device.dto.DeviceRequestDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceResponseDTO;
import eduardoamaral.devicechallenge.components.device.mapper.DeviceMapper;
import eduardoamaral.devicechallenge.shared.persistence.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    public DeviceResponseDTO create(DeviceRequestDTO request) {
        var entity = deviceMapper.toEntity(request);
        return deviceMapper.toResponse(
                deviceRepository.save(entity)
        );
    }

    public DeviceResponseDTO update(DeviceRequestDTO device) {
        return null;
    }

    public List<DeviceResponseDTO> searchDevices(DeviceQuery query) {
        return List.of();
    }

    public DeviceResponseDTO getDevice(UUID deviceId) {
        return null;
    }

    public List<DeviceResponseDTO> getAllDevices() {
        return List.of();
    }

    public boolean tryDeleteDevice(UUID deviceId) {
        return false;
    }
}
