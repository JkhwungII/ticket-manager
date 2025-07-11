package dev.jake.ticket_manager.tickets.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
public class TicketInfo {
    private int id;
    private int ticket_id;
    private int staff_id;
    private String process_detail;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_time;

    public TicketInfo(int id, int ticket_id, int staff_id, String process_detail, LocalDateTime created_time) {
        this.id = id;
        this.ticket_id = ticket_id;
        this.staff_id = staff_id;
        this.process_detail = process_detail;
        this.created_time = created_time;
    }

    public TicketInfo() {
    }
}
