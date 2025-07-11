package dev.jake.ticket_manager.device;

import lombok.Getter;

@Getter
public class Device {
    public Device(int id, String type, String CPU, String mother_board, String drive, String additional_info) {
        this.id = id;
        this.type = type;
        this.CPU = CPU;
        this.mother_board = mother_board;
        this.drive = drive;
        this.additional_info = additional_info;
    }

    private int id;
    private String type, CPU, mother_board, drive, additional_info;
}
