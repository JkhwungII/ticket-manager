package dev.jake.ticket_manager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final List<String> EXCLUDED_PATHS = List.of("/api/user/login");

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("auth".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }
        if (jwt == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Claims claims;
        try {
            claims = jwtService.parseToken(jwt);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 建立 UserDetails 物件
        var userDetails = new TicketUserDetail();
        userDetails.setUsername(claims.get("username", String.class));

        List<String> memberAuthorities= (List<String>) claims.get("authorities");

        userDetails.setId(claims.get("id", Integer.class));

        userDetails.setMemberAuthorities(memberAuthorities);

        // 放入 Security Context
        var token = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(token);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDED_PATHS.contains(request.getServletPath());
    }
}
