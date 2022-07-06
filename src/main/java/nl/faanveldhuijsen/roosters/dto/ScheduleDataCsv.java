package nl.faanveldhuijsen.roosters.dto;

import lombok.Data;
import nl.faanveldhuijsen.roosters.service.DateTimeService;

import java.time.LocalDateTime;

@Data
public class ScheduleDataCsv {

    private String email;

    private String taskName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
