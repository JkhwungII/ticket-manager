package dev.jake.ticket_manager.device;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:9000/")
@RequestMapping("api/devices")
public class DeviceController {

    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository){ this.deviceRepository = deviceRepository; }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/device")
    public void createDevice(@RequestBody Device new_device){
        deviceRepository.createDevice(new_device);
    }
    @GetMapping("/staff/device/{Device_ID}")
    public ResponseEntity<Device> getDeviceByID(@PathVariable int Device_ID){
        Optional<Device> device_query = deviceRepository.getDeviceByID(Device_ID);
        if(device_query.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(device_query.get());
    }

    @PatchMapping("/admin/device")
    public ResponseEntity<Void> updateDevice(@RequestBody Device update_device){
        if (!deviceRepository.doesDeviceExist(update_device.getId())){
            return ResponseEntity.notFound().build();
        }

        deviceRepository.updateDevice(update_device);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/device/{Device_ID}")
    public ResponseEntity<Void> deleteDevice(@PathVariable int device_ID){
        if (!deviceRepository.doesDeviceExist(device_ID)){
            return ResponseEntity.notFound().build();
        }
        deviceRepository.deleteDevice(device_ID);
        return ResponseEntity.noContent().build();
    }


}
