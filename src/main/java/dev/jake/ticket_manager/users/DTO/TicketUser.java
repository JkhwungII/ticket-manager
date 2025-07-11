package dev.jake.ticket_manager.users.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TicketUser {
    private int id;
    private String name;
    private String role;
    private String password;
    public TicketUser(){}

}
