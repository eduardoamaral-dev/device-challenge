package eduardoamaral.devicechallenge.components.device.service;

import eduardoamaral.devicechallenge.components.device.dto.DeviceQuery;
import eduardoamaral.devicechallenge.components.device.dto.DeviceRequestDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceResponseDTO;
import eduardoamaral.devicechallenge.components.device.mapper.DeviceMapper;
import eduardoamaral.devicechallenge.shared.persistence.model.DeviceEntity;
import eduardoamaral.devicechallenge.shared.persistence.repository.DeviceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

    @Transactional
    public DeviceResponseDTO update(String id, DeviceRequestDTO request) throws NoSuchElementException {
        DeviceEntity device = deviceRepository
                .findById(UUID.fromString(id))
                .orElseThrow();

        if(device.isInUse()){
            throw new IllegalStateException("Devices in use cannot be updated.");
        }

        if (request.getBrand() != null) {
            device.setBrand(request.getBrand());
        }
        if (request.getName() != null) {
            device.setName(request.getName());
        }
        if (request.getState() != null) {
            device.setState(deviceMapper.stringToDeviceState(request.getState()));
        }

        DeviceEntity updatedDevice = deviceRepository.save(device);
        return deviceMapper.toResponse(updatedDevice);
    }

    public List<DeviceResponseDTO> getByBrand(String brand) {
        var response =  deviceRepository
                .findByBrand(brand.trim())
                .stream()
                .map(deviceMapper::toResponse)
                .toList();

        if(response.isEmpty()) throw new NoSuchElementException();
        return response;
    }

    public List<DeviceResponseDTO> getByName(String name) {
        return deviceRepository
                .findByName(name.trim())
                .stream()
                .map(deviceMapper::toResponse)
                .toList();
    }

    public List<DeviceResponseDTO> searchDevices(DeviceQuery query) {
        return List.of();
    }

    public Optional<DeviceResponseDTO> getById(String deviceId) throws IllegalArgumentException{
        return deviceRepository
                .findById(UUID.fromString(deviceId))
                .map(deviceMapper::toResponse);
    }

    public List<DeviceResponseDTO> getAllDevices() {
        return deviceRepository
                .findAll()
                .stream()
                .map(deviceMapper::toResponse)
                .toList();
    }

    public void deleteDevice(String deviceId) {
        var  entity = deviceRepository.findById(UUID.fromString(deviceId)).orElseThrow();
        if(entity.isInUse()){
            throw new IllegalStateException("Devices in use cannot be deleted.");
        }
        deviceRepository.deleteById(UUID.fromString(deviceId));
    }
}
