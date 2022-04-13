package nl.faanveldhuijsen.roosters.dto;

import lombok.Data;
import nl.faanveldhuijsen.roosters.model.Task;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class ScheduleDataSlim {

    protected Long id;

    protected LocalDateTime startTime;

    protected LocalDateTime endTime;

    protected TaskDataSlim task;
}
