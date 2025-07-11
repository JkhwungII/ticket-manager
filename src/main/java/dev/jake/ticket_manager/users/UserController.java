package dev.jake.ticket_manager.users;

import dev.jake.ticket_manager.security.JwtService;
import dev.jake.ticket_manager.security.TicketUserDetail;
import dev.jake.ticket_manager.users.DTO.LoginRequest;
import dev.jake.ticket_manager.users.DTO.LoginResponse;
import dev.jake.ticket_manager.users.DTO.TicketUser;
import dev.jake.ticket_manager.users.DTO.TicketUserInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:9000/")
@RequestMapping("api/user")
public class UserController {
    private UserRepository repository;

    private AuthenticationManager authenticationManager;

    private JwtService jwtService;

    private PasswordEncoder passwordEncoder;
    public UserController(UserRepository repository, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder){
        this.repository = repository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request, HttpServletResponse response){
        Authentication token = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        Authentication auth = authenticationManager.authenticate(token);
        TicketUserDetail user = (TicketUserDetail) auth.getPrincipal();
        String jwt_token = jwtService.createLoginAccessToken(user);

        return LoginResponse.of(jwt_token,user);
    }

    @PostMapping("/admin")
    public void createUser(@RequestBody TicketUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.createUser(user);
    }

    @GetMapping("/staff/userinfo/{user_id}")
    public ResponseEntity<TicketUserInfo> getUserInfo(@PathVariable int user_id){
        Optional<TicketUserInfo> info_query = repository.getUserInfoByID(user_id);
        if (info_query.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info_query.get());
    }
}
