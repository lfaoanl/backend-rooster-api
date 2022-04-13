package nl.faanveldhuijsen.roosters.repository;

import nl.faanveldhuijsen.roosters.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<Task, Long> {

}
