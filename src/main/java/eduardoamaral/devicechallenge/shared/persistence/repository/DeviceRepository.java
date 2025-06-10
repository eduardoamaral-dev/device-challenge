package eduardoamaral.devicechallenge.shared.persistence.repository;

import eduardoamaral.devicechallenge.shared.persistence.model.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {
}
