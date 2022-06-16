package nl.faanveldhuijsen.roosters.controller;

import nl.faanveldhuijsen.roosters.dto.TaskData;
import nl.faanveldhuijsen.roosters.dto.TaskDataSlim;
import nl.faanveldhuijsen.roosters.service.TaskService;
import nl.faanveldhuijsen.roosters.utils.DefaultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TaskController {

    private final DefaultResponse response;
    private final TaskService tasks;

    public TaskController(TaskService tasks, DefaultResponse response) {
        this.tasks = tasks;
        this.response = response;
    }

    @GetMapping("/tasks")
    public ResponseEntity<Object> getTasks() {
        return response.ok(tasks.fetch());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/tasks")
    public ResponseEntity<Object> createTask(@Valid @RequestBody TaskData task, BindingResult result) {
        if (result.hasErrors()) {
            return response.fieldErrors(result);
        }
        return response.created(tasks.create(task));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Object> showTask(@PathVariable("id") Long id) {
        TaskData task = this.tasks.get(id);

        return response.ok(task);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/tasks/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable("id") Long id, @Valid @RequestBody TaskData updatedTask, BindingResult result) {
        if (result.hasErrors()) {
            return response.fieldErrors(result);
        }
        TaskDataSlim task = this.tasks.update(id, updatedTask);

        return response.ok(task);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable("id") Long id) {
        TaskDataSlim task = this.tasks.delete(id);

        if (task == null) {
            return response.notFound("Task not found");
        }

        return response.ok(task);
    }
}