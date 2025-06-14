package eduardoamaral.devicechallenge.shared.persistence.model;

import eduardoamaral.devicechallenge.components.device.dto.DeviceState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static eduardoamaral.devicechallenge.components.device.dto.DeviceState.IN_USE;


@Entity
@Table(name = "devices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String brand;
    @Enumerated(EnumType.STRING)
    private DeviceState state;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public boolean isInUse(){
        return state == IN_USE;
    }
}
