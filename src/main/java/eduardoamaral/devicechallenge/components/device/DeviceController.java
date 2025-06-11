package eduardoamaral.devicechallenge.components.device;

import eduardoamaral.devicechallenge.components.device.dto.DeviceRequestDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceResponseDTO;
import eduardoamaral.devicechallenge.components.device.service.DeviceService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @PostMapping("/add")
    public ResponseEntity<DeviceResponseDTO> addDevice(@Valid @RequestBody DeviceRequestDTO request){
        return ok(deviceService.create(request));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DeviceResponseDTO>> search(
            @RequestParam String model,
            @RequestParam String valid,
            @RequestParam String name
            ){
        return null;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DeviceResponseDTO>> getAllDevices(){
        return ok(deviceService.getAllDevices());
    }

    @GetMapping("/find/{deviceId}")
    public ResponseEntity<DeviceResponseDTO> find(@PathVariable String deviceId){
        return deviceService.getDevice(deviceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/remove/{deviceId}")
    public ResponseEntity<Object> removeDevice(@PathVariable String deviceId){
        deviceService.deleteDevice(deviceId);
        return ok().build();
    }
}
