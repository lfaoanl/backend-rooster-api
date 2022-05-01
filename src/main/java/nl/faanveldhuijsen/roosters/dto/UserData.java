package nl.faanveldhuijsen.roosters.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import nl.faanveldhuijsen.roosters.model.User;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class UserData {

    protected Long id;

    protected String name;

    @Email
    protected String email;

    protected String password;

    @Pattern(regexp = "^(USER|ADMIN)$", message = "must equal USER or ADMIN")
    protected String role;

    protected List<ScheduleDataSlim> schedules;

}
