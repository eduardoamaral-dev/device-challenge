package eduardoamaral.devicechallenge.components.device.domain.models;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class Device {
    private UUID id;
    private String name;
    private String brand;
    private DeviceState state;
    private Date createdAt;

    public boolean isInUse(){
        return state == DeviceState.IN_USE;
    }
}
