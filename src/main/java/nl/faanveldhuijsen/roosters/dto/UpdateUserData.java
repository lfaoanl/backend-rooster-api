package nl.faanveldhuijsen.roosters.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UpdateUserData {

    @Email
    public String email;

    public String name;

    public String password;
}
