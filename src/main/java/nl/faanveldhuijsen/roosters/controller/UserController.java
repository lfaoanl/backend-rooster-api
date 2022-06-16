package nl.faanveldhuijsen.roosters.controller;

import nl.faanveldhuijsen.roosters.dto.ScheduleData;
import nl.faanveldhuijsen.roosters.dto.UpdateUserData;
import nl.faanveldhuijsen.roosters.dto.UserData;
import nl.faanveldhuijsen.roosters.dto.UserDataSlim;
import nl.faanveldhuijsen.roosters.dto.mapper.IUserMapper;
import nl.faanveldhuijsen.roosters.model.User;
import nl.faanveldhuijsen.roosters.service.ScheduleService;
import nl.faanveldhuijsen.roosters.service.UserService;
import nl.faanveldhuijsen.roosters.utils.DefaultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
public class UserController {

    @Autowired
    private DefaultResponse response;

    @Autowired
    private UserService user;

    @Autowired
    private ScheduleService schedule;

    @Autowired
    private IUserMapper mapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Object> getUsers() {
        return new ResponseEntity<>(user.fetch(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserData user, BindingResult result) {
        if (result.hasErrors()) {
            return response.fieldErrors(result);
        }
        UserDataSlim newUser = this.user.create(user);
        return this.response.created(newUser);
    }

    @GetMapping("/users/me")
    public ResponseEntity<Object> me() {
        UserData authenticatedUser = user.getAuthenticatedUser();

        UserDataSlim me = new UserDataSlim();
        me.setId(authenticatedUser.getId());
        me.setName(authenticatedUser.getName());
        me.setEmail(authenticatedUser.getEmail());
        me.setRole(authenticatedUser.getRole());

        return ResponseEntity.ok(me);
    }

    @PostMapping("/users/me")
    public ResponseEntity<Object> updateMe(@Valid @RequestBody UpdateUserData updatedUser, BindingResult result) {
        if (result.hasErrors()) {
            return response.fieldErrors(result);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getPrincipal() instanceof UserDetails) {
            UserDetails ud = (UserDetails) auth.getPrincipal();
            UserData me = this.user.get(ud.getUsername());

            // values parsed from request body
            me.setEmail(updatedUser.getEmail());
            me.setName(updatedUser.getName());
            me.setPassword(updatedUser.getPassword());

            // Update and return user
            UserDataSlim user = this.user.update(me.getId(), me);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity<Object> showUser(@PathVariable("id") Long id) {
        UserData user = this.user.get(id);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable("id") Long id) {
        UserDataSlim task = this.user.delete(id);

        return response.ok(task);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserData updatedUser, BindingResult result) {
        if (result.hasErrors()) {
            return response.fieldErrors(result);
        }
        UserDataSlim user = this.user.update(id, updatedUser);

        return response.ok(user);
    }

    /**
     * Find schedules of selected user. Throws notFound error when the authenticated
     * user is not of role admin and tries to fetch someone else's schedules
     * @param userId
     * @throws nl.faanveldhuijsen.roosters.utils.exceptions.ResourceNotFound
     * @return List of schedules from user
     */
    @GetMapping("/users/{id}/schedules")
    public ResponseEntity<Object> getUserSchedules(@PathVariable("id") Long userId) {

        UserData data = this.user.getAuthenticatedUser(userId);
        User user = mapper.dataToEntity(data);

        Collection<ScheduleData> schedules = schedule.getSchedulesFromUser(user);

        return response.ok(schedules);
    }
}
