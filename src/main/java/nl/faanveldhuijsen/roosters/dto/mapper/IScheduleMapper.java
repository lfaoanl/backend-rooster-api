package nl.faanveldhuijsen.roosters.dto.mapper;

import nl.faanveldhuijsen.roosters.dto.ScheduleData;
import nl.faanveldhuijsen.roosters.dto.ScheduleDataSlim;
import nl.faanveldhuijsen.roosters.dto.UserData;
import nl.faanveldhuijsen.roosters.dto.UserDataSlim;
import nl.faanveldhuijsen.roosters.model.Schedule;
import nl.faanveldhuijsen.roosters.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IScheduleMapper {

    Schedule dataToEntity(ScheduleData data);

    Schedule dataToEntity(ScheduleDataSlim data);

    ScheduleData entityToData(Schedule entity);

    ScheduleDataSlim entityToDataSlim(Schedule entity);

    List<ScheduleData> entityListToDataList(List<Schedule> schedules);
}
