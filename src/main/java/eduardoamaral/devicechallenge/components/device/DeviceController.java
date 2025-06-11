package eduardoamaral.devicechallenge.components.device;

import eduardoamaral.devicechallenge.components.device.dto.DeviceRequestDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceResponseDTO;
import eduardoamaral.devicechallenge.components.device.service.DeviceService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @PostMapping("/add")
    public ResponseEntity<DeviceResponseDTO> addDevice(@Valid @RequestBody DeviceRequestDTO request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(deviceService.create(request));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DeviceResponseDTO>> search( // TODO: work on that later
            @RequestParam String brand,
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
        return deviceService.getById(deviceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/find/brand/{brand}")
    public ResponseEntity<List<DeviceResponseDTO>> getByBrand(@PathVariable String brand){
        return ok(deviceService.getByBrand(brand));
    }

    @GetMapping("/find/name/{name}")
    public ResponseEntity<List<DeviceResponseDTO>> getByName(@PathVariable String name){
        return ok(deviceService.getByName(name));
    }

    @DeleteMapping("/remove/{deviceId}")
    public ResponseEntity<Object> removeDevice(@PathVariable String deviceId){
        deviceService.deleteDevice(deviceId);
        return ok().build();
    }

    @PatchMapping("/update/{deviceId}")
    public ResponseEntity<DeviceResponseDTO> find(@PathVariable String deviceId, @RequestBody DeviceRequestDTO request){
        return ok(deviceService.update(deviceId, request));
    }
}
