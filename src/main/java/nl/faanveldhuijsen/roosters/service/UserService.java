package nl.faanveldhuijsen.roosters.service;

import lombok.RequiredArgsConstructor;
import nl.faanveldhuijsen.roosters.dto.UserData;
import nl.faanveldhuijsen.roosters.dto.UserDataSlim;
import nl.faanveldhuijsen.roosters.dto.mapper.IUserMapper;
import nl.faanveldhuijsen.roosters.model.User;
import nl.faanveldhuijsen.roosters.repository.IUserRepository;
import nl.faanveldhuijsen.roosters.utils.exceptions.ResourceNotFound;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements ICrudService<UserData, UserDataSlim> {

    private final IUserRepository repo;

    private final IUserMapper mapper;

    @Override
    public UserData create(UserData data) {
        User userSave = repo.save(mapper.dataToEntity(data));
        return mapper.entityToData(userSave);
    }

    @Override
    public UserData update(Long id, UserData data) {
        User user = repo.findById(id).orElse(null);

        if (user != null) {
            user.setName(data.getName());
            user.setEmail(data.getEmail());
            user.setPassword(data.getPassword());

            return mapper.entityToData(repo.save(user));
        }

        notFound();
        return null;
    }

    @Override
    public UserData get(Long id) {
        Optional<User> query = repo.findById(id);

        if (query.isEmpty()) {
            notFound();
        }

        return mapper.entityToData(query.get());
    }

    @Override
    public UserData delete(Long id) {
        Optional<User> query = repo.findById(id);

        if (query.isEmpty()) {
            notFound();
        }
        repo.deleteById(id);

        return mapper.entityToData(query.get());
    }

    @Override
    public Collection<UserDataSlim> fetch() {
        Collection<User> entities = repo.findAll();
        ArrayList<UserDataSlim> users = new ArrayList<>();

        entities.forEach(user -> users.add(mapper.entityToDataSlim(user)));

        return users;
    }

    public void notFound() {
        throw new ResourceNotFound("User not found");
    }
}
