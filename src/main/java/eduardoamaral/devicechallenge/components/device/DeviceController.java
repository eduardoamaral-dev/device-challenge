package eduardoamaral.devicechallenge.components.device;

import eduardoamaral.devicechallenge.components.device.dto.DeviceRequestDTO;
import eduardoamaral.devicechallenge.components.device.dto.DeviceResponseDTO;
import eduardoamaral.devicechallenge.components.device.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
@Tag(name = "Device Controller", description = "CRUD endpoint to manage devices")
public class DeviceController {
    private final DeviceService deviceService;

    @PostMapping("/add")
    @Operation(summary = "Create and return the created device")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = {}
            )
    })
    public ResponseEntity<DeviceResponseDTO> addDevice(@Valid @RequestBody DeviceRequestDTO request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(deviceService.create(request));
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<DeviceResponseDTO>> search( // TODO: work on that later
//            @RequestParam String brand,
//            @RequestParam String valid,
//            @RequestParam String name
//            ){
//        return null;
//    }

    @GetMapping("/getAll")
    @Operation(summary = "Returns a list of all devices")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid params"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found"
            )
    })
    public ResponseEntity<List<DeviceResponseDTO>> getAllDevices(){
        return ok(deviceService.getAllDevices());
    }

    @GetMapping("/find/{deviceId}")
    @Operation(summary = "Find device by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid params"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found"
            )
    })
    public ResponseEntity<DeviceResponseDTO> find(@PathVariable String deviceId){
        return deviceService.getById(deviceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/find/brand/{brand}")
    @Operation(summary = "Find device by brand")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid params"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found"
            )
    })
    public ResponseEntity<List<DeviceResponseDTO>> getByBrand(@PathVariable String brand){
        return ok(deviceService.getByBrand(brand));
    }

    @GetMapping("/find/state/{state}")
    @Operation(summary = "Find device by state")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid params"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found"
            )
    })
    public ResponseEntity<List<DeviceResponseDTO>> getByState(@PathVariable String state){
        return ok(deviceService.getByState(state));
    }

    @DeleteMapping("/remove/{deviceId}")
    @Operation(summary = "Remove a device by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid params"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found"
            )
    })
    public ResponseEntity<Object> removeDevice(@PathVariable String deviceId){
        deviceService.deleteDevice(deviceId);
        return ok().build();
    }

    @PatchMapping("/update/{deviceId}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found"
            )
    })
    @Operation(summary = "Update device by id and return the updated device")
    public ResponseEntity<DeviceResponseDTO> find(@PathVariable String deviceId, @RequestBody DeviceRequestDTO request){
        return ok(deviceService.update(deviceId, request));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationExceptions(ValidationException ex) {
        return ResponseEntity.unprocessableEntity().body("Request is invalid. Error: " + ex.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleValidationExceptions(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body("Some error has occurred during device search. Error message: " + ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleValidationExceptions(NoSuchElementException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleValidationExceptions(IllegalStateException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleValidationExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
