package nl.faanveldhuijsen.roosters.controller;

import nl.faanveldhuijsen.roosters.dto.ScheduleData;
import nl.faanveldhuijsen.roosters.dto.TaskData;
import nl.faanveldhuijsen.roosters.dto.UserData;
import nl.faanveldhuijsen.roosters.dto.mapper.IUserMapper;
import nl.faanveldhuijsen.roosters.model.Schedule;
import nl.faanveldhuijsen.roosters.service.UserService;
import nl.faanveldhuijsen.roosters.utils.DefaultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private DefaultResponse response;

    @Autowired
    private UserService user;

    @Autowired
    private IUserMapper mapper;

    @GetMapping("/users")
    public ResponseEntity<Object> getUsers() {
        return new ResponseEntity<>(user.fetch(), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserData user, BindingResult result) {
        if (result.hasErrors()) {
            return response.fieldErrors(result);
        }
        UserData newUser = this.user.create(user);
        return this.response.created(newUser);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Object> showUser(@PathVariable("id") Long id) {
        UserData user = this.user.get(id);

        if (user == null) {
            return response.notFound("User not found");
        }

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable("id") Long id) {
        UserData task = this.user.delete(id);

        if (task == null) {
            return response.notFound("User not found");
        }

        return response.ok(task);
    }

    @PostMapping("/users/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserData updatedUser, BindingResult result) {
        if (result.hasErrors()) {
            return response.fieldErrors(result);
        }
        UserData user = this.user.update(id, updatedUser);

        return response.ok(user);
    }

    @GetMapping("/users/{id}/schedules")
    public ResponseEntity<Object> getUserSchedules(@PathVariable("id") Long userId) {
        List<Schedule> schedules = mapper.dataToEntity(this.user.get(userId)).getSchedules();

        return response.ok(schedules);
    }
}
