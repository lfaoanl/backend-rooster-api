package nl.faanveldhuijsen.roosters.controller;

import nl.faanveldhuijsen.roosters.dto.AuthData;
import nl.faanveldhuijsen.roosters.dto.TokenData;
import nl.faanveldhuijsen.roosters.service.JwtService;
import nl.faanveldhuijsen.roosters.utils.DefaultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    DefaultResponse response;

    @PostMapping("/auth")
    public ResponseEntity<Object> signIn(@RequestBody AuthData authDto) {
        UsernamePasswordAuthenticationToken up =
                new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword());
        Authentication auth = authManager.authenticate(up);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        TokenData token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION,
                        String.join(" ", token.getToken_type(), token.getToken()))
                .body(token);
    }
}
