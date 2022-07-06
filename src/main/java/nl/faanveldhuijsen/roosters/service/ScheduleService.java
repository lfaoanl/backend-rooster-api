package nl.faanveldhuijsen.roosters.service;

import lombok.RequiredArgsConstructor;
import nl.faanveldhuijsen.roosters.dto.ScheduleData;
import nl.faanveldhuijsen.roosters.dto.ScheduleDataCsv;
import nl.faanveldhuijsen.roosters.dto.mapper.IScheduleMapper;
import nl.faanveldhuijsen.roosters.dto.mapper.ITaskMapper;
import nl.faanveldhuijsen.roosters.dto.mapper.IUserMapper;
import nl.faanveldhuijsen.roosters.model.Schedule;
import nl.faanveldhuijsen.roosters.model.Task;
import nl.faanveldhuijsen.roosters.model.User;
import nl.faanveldhuijsen.roosters.repository.IScheduleRepository;
import nl.faanveldhuijsen.roosters.repository.ITaskRepository;
import nl.faanveldhuijsen.roosters.repository.IUserRepository;
import nl.faanveldhuijsen.roosters.utils.exceptions.ResourceNotFound;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

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

    public HashMap<String, List<?>> createFromCsv(List<ScheduleDataCsv> csvData) {
        List<ScheduleData> success = new ArrayList<>();
        List<ScheduleDataCsv> failed = new ArrayList<>();

        for (ScheduleDataCsv data : csvData) {
            ScheduleData scheduleData = new ScheduleData();

            Optional<User> userByEmail = userRepo.findUserByEmail(data.getEmail());
            Optional<Task> taskByName = taskRepo.findTaskByName(data.getTaskName());

            if (userByEmail.isEmpty() || taskByName.isEmpty()) {
                failed.add(data);
                continue;
            }

            scheduleData.setUser(userMapper.entityToDataSlim(userByEmail.get()));
            scheduleData.setTask(taskMapper.entityToDataSlim(taskByName.get()));
            scheduleData.setStartTime(data.getStartTime());
            scheduleData.setEndTime(data.getEndTime());

            success.add(create(scheduleData));
        }

        HashMap<String, List<?>> status = new HashMap<>();

        status.put("success", success);
        status.put("failed", failed);

        return status;
    }
}
