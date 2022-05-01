package nl.faanveldhuijsen.roosters.repository;

import nl.faanveldhuijsen.roosters.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);


}
