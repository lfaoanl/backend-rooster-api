package nl.faanveldhuijsen.roosters.model;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    public Long id;

    public String name;

    @Column(unique = true)
    public String email;

    protected String password;

    public String role;

    @OneToMany(mappedBy = "user")
    public List<Schedule> schedules;

    @OneToMany(mappedBy = "user")
    public List<DayOff> daysOff;

    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public void setPassword(String password) {
        this.password = encoder().encode(password);
    }

}
