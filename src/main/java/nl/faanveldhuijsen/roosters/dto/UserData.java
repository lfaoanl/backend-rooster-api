package nl.faanveldhuijsen.roosters.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.List;

@Data
public class UserData {

    protected Long id;

    protected String name;

    @Email
    protected String email;

    @JsonIgnore
    protected String password;

    protected List<ScheduleDataSlim> schedules;

}
