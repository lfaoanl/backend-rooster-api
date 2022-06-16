package nl.faanveldhuijsen.roosters.service;

import lombok.RequiredArgsConstructor;
import nl.faanveldhuijsen.roosters.dto.TaskData;
import nl.faanveldhuijsen.roosters.dto.TaskDataSlim;
import nl.faanveldhuijsen.roosters.dto.mapper.ITaskMapper;
import nl.faanveldhuijsen.roosters.model.Task;
import nl.faanveldhuijsen.roosters.repository.ITaskRepository;
import nl.faanveldhuijsen.roosters.utils.exceptions.ResourceNotFound;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService implements ICrudService<TaskData, TaskDataSlim> {

    private final ITaskRepository repo;

    private final ITaskMapper mapper;

    @Override
    public TaskDataSlim create(TaskData data) {
        Task save = repo.save(mapper.dataToEntity(data));
        return mapper.entityToDataSlim(save);
    }

    @Override
    public TaskDataSlim update(Long id, TaskData data) {
        Task task = repo.findById(id).orElse(null);

        if (task != null) {
            task.setName(data.getName());

            return mapper.entityToDataSlim(repo.save(task));
        }

        notFound();
        return null;
    }

    @Override
    public TaskData get(Long id) {
        Optional<Task> query = repo.findById(id);

        if (query.isEmpty()) {
            notFound();
        }

        return mapper.entityToData(query.get());
    }

    @Override
    public TaskDataSlim delete(Long id) {
        Optional<Task> query = repo.findById(id);

        if (query.isEmpty()) {
            notFound();
        }
        repo.deleteById(id);

        TaskDataSlim taskData = mapper.entityToDataSlim(query.get());
        return taskData;
    }

    @Override
    public Collection<TaskDataSlim> fetch() {
        Collection<Task> entities = repo.findAll();
        ArrayList<TaskDataSlim> tasks = new ArrayList<>();

        entities.forEach(task -> tasks.add(mapper.entityToDataSlim(task)));

        return tasks;
    }

    public void notFound() {
        throw new ResourceNotFound("Task not found.");
    }
}
