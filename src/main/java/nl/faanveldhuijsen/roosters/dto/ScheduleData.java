package nl.faanveldhuijsen.roosters.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ScheduleData {

    protected Long id;

    protected LocalDateTime startTime;

    protected LocalDateTime endTime;

    @NotNull
    protected UserDataSlim user;

    @NotNull
    protected TaskDataSlim task;

}
