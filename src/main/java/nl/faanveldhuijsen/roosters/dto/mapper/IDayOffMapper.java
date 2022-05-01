package nl.faanveldhuijsen.roosters.dto.mapper;

import nl.faanveldhuijsen.roosters.dto.DayOffData;
import nl.faanveldhuijsen.roosters.dto.DayOffDataSlim;
import nl.faanveldhuijsen.roosters.model.DayOff;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IDayOffMapper {

    DayOff dataToEntity(DayOffData data);

    DayOff dataToEntity(DayOffDataSlim data);

    DayOffData entityToData(DayOff entity);

    DayOffDataSlim entityToDataSlim(DayOff entity);

    List<DayOffData> entityListToDataList(List<DayOff> daysOff);
}
