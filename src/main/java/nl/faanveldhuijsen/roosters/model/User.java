package nl.faanveldhuijsen.roosters.model;

import lombok.Data;

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

    @OneToMany(mappedBy = "user")
    public List<Schedule> schedules;

}
