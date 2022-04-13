package nl.faanveldhuijsen.roosters.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import nl.faanveldhuijsen.roosters.dto.TaskData;
import nl.faanveldhuijsen.roosters.dto.TaskDataSlim;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "task")
    private List<Schedule> schedules;

}
