package nl.faanveldhuijsen.roosters.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import nl.faanveldhuijsen.roosters.dto.ScheduleData;
import nl.faanveldhuijsen.roosters.dto.ScheduleDataSlim;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    public Task task;

    public LocalDateTime startTime;

    public LocalDateTime endTime;

}
