package dev.jake.ticket_manager.users.DTO;

import dev.jake.ticket_manager.security.TicketUserDetail;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
@Getter
public class LoginResponse implements Serializable {
    private int id;
    private String username;
    private List<String> authorities;

    public static LoginResponse of(TicketUserDetail user) {
        var res = new LoginResponse();
        res.id = user.getID();
        res.username = user.getUsername();
        res.authorities = user.getUserAuthorities();
        return res;
    }



}
