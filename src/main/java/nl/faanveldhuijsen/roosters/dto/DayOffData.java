package nl.faanveldhuijsen.roosters.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class DayOffData {


    protected Long id;

    @NotNull
    protected LocalDateTime startTime;

    @NotNull
    protected LocalDateTime endTime;

    @NotNull
    protected String reason;

    protected UserDataSlim user;

}
