package nl.faanveldhuijsen.roosters.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class DayOffDataSlim {

    protected Long id;

    protected LocalDateTime startTime;

    protected LocalDateTime endTime;

}
