package nl.faanveldhuijsen.roosters.service;

import lombok.RequiredArgsConstructor;
import nl.faanveldhuijsen.roosters.dto.ScheduleData;
import nl.faanveldhuijsen.roosters.dto.mapper.IScheduleMapper;
import nl.faanveldhuijsen.roosters.dto.mapper.ITaskMapper;
import nl.faanveldhuijsen.roosters.dto.mapper.IUserMapper;
import nl.faanveldhuijsen.roosters.model.Schedule;
import nl.faanveldhuijsen.roosters.model.User;
import nl.faanveldhuijsen.roosters.repository.IScheduleRepository;
import nl.faanveldhuijsen.roosters.repository.ITaskRepository;
import nl.faanveldhuijsen.roosters.repository.IUserRepository;
import nl.faanveldhuijsen.roosters.utils.exceptions.ResourceNotFound;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ScheduleService implements ICrudService<ScheduleData, ScheduleData> {

    private final IScheduleRepository repo;
    private final IScheduleMapper mapper;

    private final IUserMapper userMapper;
    private final IUserRepository userRepo;

    private final ITaskMapper taskMapper;
    private final ITaskRepository taskRepo;

    @Override
    public ScheduleData create(ScheduleData data) {
        Schedule schedule = repo.save(mapper.dataToEntity(data));

        repopulateRelationFields(schedule);

        return this.mapper.entityToData(schedule);
    }

    @Override
    public ScheduleData update(Long id, ScheduleData data) {
        Schedule schedule = repo.findById(id).orElse(null);

        if (schedule != null) {
            schedule.setStartTime(data.getStartTime());
            schedule.setEndTime(data.getEndTime());
            schedule.setUser(userMapper.dataToEntity(data.getUser()));
            schedule.setTask(taskMapper.dataToEntity(data.getTask()));

            repopulateRelationFields(schedule);
            return mapper.entityToData(repo.save(schedule));
        }

        notFound();
        return null;
    }

    @Override
    public ScheduleData get(Long id) {
        Optional<Schedule> query = repo.findById(id);

        if (query.isEmpty()) {
            notFound();
        }
        return mapper.entityToData(query.get());
    }

    @Override
    public ScheduleData delete(Long id) {
        Optional<Schedule> query = repo.findById(id);

        if (query.isEmpty()) {
            notFound();
        }
        repo.deleteById(id);

        return mapper.entityToData(query.get());
    }

    public void notFound() {
        throw new ResourceNotFound("Schedule not found.");
    }

    @Override
    public Collection<ScheduleData> fetch() {
        Collection<Schedule> entities = repo.findAll();
        ArrayList<ScheduleData> schedules = new ArrayList<>();

        entities.forEach(schedule -> schedules.add(mapper.entityToData(schedule)));

        return schedules;
    }

    public Collection<ScheduleData> inBetweenDates(LocalDateTime start, LocalDateTime end) {

        List<Schedule> inBetween = this.repo.findAllByStartTimeBetween(start, end);
        return this.mapper.entityListToDataList(inBetween);
    }

    private void repopulateRelationFields(Schedule schedule) {
        // Repopulate relation fields for easy frontend manipulation
        Long taskId = schedule.getTask().getId();
        schedule.setTask(taskRepo.findById(taskId).orElse(schedule.getTask()));
        Long userId = schedule.getUser().getId();
        schedule.setUser(userRepo.findById(userId).orElse(schedule.getUser()));
    }

    public Collection<ScheduleData> getSchedulesFromUser(User user) {
        return mapper.entityListToDataList(repo.findAllByUser(user));
    }
}
