package nl.faanveldhuijsen.roosters.repository;

import nl.faanveldhuijsen.roosters.model.DayOff;
import nl.faanveldhuijsen.roosters.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IDayOffRepository extends JpaRepository<DayOff, Long> {

    List<DayOff> findAllByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    List<DayOff> findAllByUser(User user);

    Optional<DayOff> findByIdAndUser(Long id, User user);

    List<DayOff> findAllByUserAndStartTimeBetween(User user, LocalDateTime start, LocalDateTime end);

}
