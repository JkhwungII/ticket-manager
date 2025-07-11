package dev.jake.ticket_manager.tickets.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.jake.ticket_manager.device.Device;
import dev.jake.ticket_manager.users.DTO.TicketUserInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StaffListTicket {

    private int id;
    private TicketUserInfo info;
    private String issue;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_time;
    private String state;
    public StaffListTicket(){}
    public StaffListTicket(int id, TicketUserInfo info, String issue, LocalDateTime created_time, String state) {
        this.id = id;
        this.info = info;
        this.issue = issue;
        this.created_time = created_time;
        this.state = state;
    }

}
