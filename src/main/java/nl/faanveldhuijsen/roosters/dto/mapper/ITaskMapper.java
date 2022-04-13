package nl.faanveldhuijsen.roosters.dto.mapper;

import nl.faanveldhuijsen.roosters.dto.ScheduleData;
import nl.faanveldhuijsen.roosters.dto.TaskData;
import nl.faanveldhuijsen.roosters.dto.TaskDataSlim;
import nl.faanveldhuijsen.roosters.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ITaskMapper {

    Task dataToEntity(TaskData data);

    Task dataToEntity(TaskDataSlim data);

    TaskData entityToData(Task entity);

    TaskDataSlim entityToDataSlim(Task entity);

}
