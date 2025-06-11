package eduardoamaral.devicechallenge.shared.persistence.repository;

import eduardoamaral.devicechallenge.components.device.dto.DeviceResponseDTO;
import eduardoamaral.devicechallenge.shared.persistence.model.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {
    @Query("SELECT d FROM DeviceEntity d WHERE d.name = :name")
    List<DeviceEntity> findByName(@Param("name") String name);
    @Query("SELECT d FROM DeviceEntity d WHERE d.brand = :brand")
    List<DeviceEntity> findByBrand(@Param("brand") String brand);
}
