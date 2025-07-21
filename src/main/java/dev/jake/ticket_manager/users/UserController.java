package dev.jake.ticket_manager.users;

import dev.jake.ticket_manager.security.JwtService;
import dev.jake.ticket_manager.security.TicketUserDetail;
import dev.jake.ticket_manager.users.DTO.LoginRequest;
import dev.jake.ticket_manager.users.DTO.LoginResponse;
import dev.jake.ticket_manager.users.DTO.TicketUser;
import dev.jake.ticket_manager.users.DTO.TicketUserInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import jakarta.servlet.http.Cookie;

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
    public void login(@RequestBody LoginRequest request, HttpServletResponse response){
        Authentication token = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        Authentication auth = authenticationManager.authenticate(token);
        TicketUserDetail user = (TicketUserDetail) auth.getPrincipal();
        String jwt_token = jwtService.createLoginAccessToken(user);

        // Set JWT as an HTTP-only cookie using ResponseCookie builder for SameSite support
        ResponseCookie cookie = ResponseCookie.from("auth", jwt_token)
            .httpOnly(true)
            .secure(true)        // add this back
            .sameSite("None")    // keep this
            .path("/")
            .maxAge(60 * 60)
            .build();
        response.addHeader("Set-Cookie", cookie.toString());

    }

    @GetMapping("/me")
    public LoginResponse getUserInfo(Authentication authentication) {
        TicketUserDetail userDetails = (TicketUserDetail) authentication.getPrincipal();
        return LoginResponse.of(userDetails);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        // To clear the cookie, we must build a cookie with the same attributes (path, domain, secure, sameSite)
        // and set maxAge to 0.
        ResponseCookie cookie = ResponseCookie.from("auth", "")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(0)
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
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
