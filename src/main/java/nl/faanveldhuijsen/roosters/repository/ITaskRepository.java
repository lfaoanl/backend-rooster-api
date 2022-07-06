package nl.faanveldhuijsen.roosters.repository;

import nl.faanveldhuijsen.roosters.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findTaskByName(String name);
}
