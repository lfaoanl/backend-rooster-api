package nl.faanveldhuijsen.roosters.repository;

import nl.faanveldhuijsen.roosters.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {

}
