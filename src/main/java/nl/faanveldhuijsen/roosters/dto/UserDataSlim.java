package nl.faanveldhuijsen.roosters.dto;

import lombok.Data;
import javax.validation.constraints.Email;

@Data
public class UserDataSlim {

    protected Long id;

    protected String name;

    @Email
    protected String email;

}
