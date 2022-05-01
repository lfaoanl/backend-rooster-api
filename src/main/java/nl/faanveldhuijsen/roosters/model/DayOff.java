package nl.faanveldhuijsen.roosters.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "off_days")
public class DayOff {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    public LocalDateTime startTime;

    public LocalDateTime endTime;

    public String reason;
}
