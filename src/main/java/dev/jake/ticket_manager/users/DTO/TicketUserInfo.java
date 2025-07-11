package dev.jake.ticket_manager.users.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TicketUserInfo {
    private String name;
    private String phone_number;
    public TicketUserInfo(){}

    public TicketUserInfo(String name, String phone_number) {
        this.name = name;
        this.phone_number = phone_number;
    }
}
