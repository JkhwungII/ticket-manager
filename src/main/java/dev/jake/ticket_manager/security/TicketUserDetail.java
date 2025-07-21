package dev.jake.ticket_manager.security;

import dev.jake.ticket_manager.users.DTO.TicketUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TicketUserDetail implements UserDetails {
    private int id;
    private String username;
    private String password;

    private List<String> memberAuthorities;

    public TicketUserDetail() {
    }

    public TicketUserDetail(TicketUser user) {
        this.id = user.getId();
        this.username = user.getName();
        this.password = user.getPassword();
        this.memberAuthorities = Arrays.asList(user.getRole().split(","));
    }

    public int getID() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }


    public List<String> getUserAuthorities() {
        return this.memberAuthorities;
    }

    public Collection<? extends SimpleGrantedAuthority> getAuthorities() {
        return this.memberAuthorities
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
    public void setId(int id) { this.id = id; }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setMemberAuthorities(List<String> memberAuthorities) {
        this.memberAuthorities = memberAuthorities;
    }
}
