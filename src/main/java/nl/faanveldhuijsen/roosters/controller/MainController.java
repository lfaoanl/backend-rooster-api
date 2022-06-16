package nl.faanveldhuijsen.roosters.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String index() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getPrincipal() instanceof UserDetails) {
            UserDetails ud = (UserDetails) auth.getPrincipal();
            return "Hello " + ud.getUsername() + "! " + auth.getAuthorities().toString();
        }
        else {
            return "Hello stranger!";
        }
    }

    /**
     * 418 I'm a teapot (RFC 2324)
     *
     * This code was defined in 1998 as one of the traditional IETF April Fools' jokes,
     * in RFC 2324, Hyper Text Coffee Pot Control Protocol, and is not expected to be
     * implemented by actual HTTP servers. However, known implementations do exist.
     *
     * @return TeapotResponse
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/teapot")
    public ResponseEntity<String> imATeapot() {
        return new ResponseEntity<>("I'm a teapot!", HttpStatus.I_AM_A_TEAPOT);
    }
}
