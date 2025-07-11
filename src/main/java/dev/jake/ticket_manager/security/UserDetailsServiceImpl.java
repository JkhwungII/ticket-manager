package dev.jake.ticket_manager.security;

import dev.jake.ticket_manager.users.DTO.TicketUser;
import dev.jake.ticket_manager.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<TicketUser> ticketUserQuery = userRepository.getUserByUsername(username);
        if(ticketUserQuery.isEmpty()){
            throw new UsernameNotFoundException("Can't find user: " + username);
        }
        TicketUser ticketUser = ticketUserQuery.get();
        return new TicketUserDetail(ticketUser);
    }
}
