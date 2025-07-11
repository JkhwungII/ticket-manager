package dev.jake.ticket_manager.device;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Optional;

@Repository
public class DeviceRepository {
    private final JdbcClient jdbcClient;

    public DeviceRepository(JdbcClient jdbcClient){ this.jdbcClient = jdbcClient; }

    public boolean doesDeviceExist(int device_ID){
        return jdbcClient.sql("SELECT EXISTS(SELECT 1 FROM device WHERE id = :device_ID)")
                .param("device_ID", device_ID)
                .query(Boolean.class)
                .single();
    }

    public Optional<Device> getDeviceByID(int device_ID){
        return jdbcClient.sql("SELECT * FROM device WHERE id = :device_ID;")
                .param("device_ID",device_ID)
                .query(Device.class)
                .optional();
    }

    public void createDevice(Device device){
        var updated = jdbcClient.sql("""
        INSERT INTO device (type, CPU, mother_board, drive, additional_info) 
        VALUES (:type, :cpu, :motherBoard, :drive, :additionalInfo)
        """)
                .param("type", device.getType())
                .param("cpu", device.getCPU())
                .param("motherBoard", device.getMother_board())
                .param("drive", device.getDrive())
                .param("additionalInfo", device.getAdditional_info())
                .update();

        Assert.state(updated == 1, "Failed to create Device: " + device.getId());
    }

    public void updateDevice(Device device) {
        String sql_query = "UPDATE device SET " +
                "type = :type, " +
                "CPU = :cpu, " +
                "mother_board = :motherBoard, " +
                "drive = :drive, " +
                "additional_info = :additionalInfo " +
                "WHERE id = :id";

        var updated = jdbcClient.sql(sql_query)
                .param("type", device.getType())
                .param("cpu", device.getCPU())
                .param("motherBoard", device.getMother_board())
                .param("drive", device.getDrive())
                .param("additionalInfo", device.getAdditional_info())
                .param("id", device.getId())
                .update();

        Assert.state(updated == 1, "Failed to update Device with id: " + device.getId());
    }

    public void deleteDevice(int deviceId) {
        String sql_query = "DELETE FROM device WHERE id = ?";

        var deleted = jdbcClient.sql(sql_query)
                .param(deviceId)
                .update();

        Assert.state(deleted == 1, "Failed to delete Device with id: " + deviceId);
    }
}
