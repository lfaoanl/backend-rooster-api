package nl.faanveldhuijsen.roosters.dto;

import lombok.Data;

@Data
public class TokenData {


    private final String token_type;

    private final String token;

    private final long expires_at;

}
