package nl.faanveldhuijsen.roosters.dto.mapper;

import nl.faanveldhuijsen.roosters.dto.UserData;
import nl.faanveldhuijsen.roosters.dto.UserDataSlim;
import nl.faanveldhuijsen.roosters.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    User dataToEntity(UserData data);

    User dataToEntity(UserDataSlim data);

    UserData entityToData(User user);

    UserDataSlim entityToDataSlim(User user);

}
