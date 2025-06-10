package eduardoamaral.devicechallenge.shared.persistence.model;

import eduardoamaral.devicechallenge.components.device.domain.models.DeviceState;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "devices")
@Data
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String brand;
    @Enumerated(EnumType.STRING)
    private DeviceState state;
    private LocalDateTime createdAt;
}
