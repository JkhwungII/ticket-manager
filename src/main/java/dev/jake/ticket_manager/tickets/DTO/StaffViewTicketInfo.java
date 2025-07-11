package dev.jake.ticket_manager.tickets.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class StaffViewTicketInfo {
    private int id;
    private String staff_name;
    private String process_detail;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_time;

    public StaffViewTicketInfo(int id, String staff_name, String process_detail, LocalDateTime created_time) {
        this.id = id;
        this.staff_name = staff_name;
        this.process_detail = process_detail;
        this.created_time = created_time;
    }
}
