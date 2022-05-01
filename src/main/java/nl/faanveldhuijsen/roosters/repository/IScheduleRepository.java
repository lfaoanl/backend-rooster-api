package nl.faanveldhuijsen.roosters.repository;

import nl.faanveldhuijsen.roosters.model.Schedule;
import nl.faanveldhuijsen.roosters.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface IScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Schedule> findAllByUser(User user);

}
