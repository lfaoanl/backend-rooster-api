package nl.faanveldhuijsen.roosters.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TaskData {

    private Long id;

    private String name;

    private List<ScheduleDataSlim> schedules;
}
