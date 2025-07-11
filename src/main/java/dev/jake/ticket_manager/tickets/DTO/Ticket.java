package dev.jake.ticket_manager.tickets.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class Ticket {
    private int id;
    private int issuerId;
    private String issue;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_time;
    private int device_id;
    private String state;

    public Ticket() {
    }

    public Ticket(int id, int issuerId, String issue, int device_id, LocalDateTime created_time, String state) {
        this.id = id;
        this.issuerId = issuerId;
        this.issue = issue;
        this.device_id = device_id;
        this.created_time = created_time;
        this.state = state;
    }


}
