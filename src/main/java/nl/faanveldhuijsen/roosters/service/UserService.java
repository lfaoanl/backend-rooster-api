package nl.faanveldhuijsen.roosters.service;

import lombok.RequiredArgsConstructor;
import nl.faanveldhuijsen.roosters.dto.UserData;
import nl.faanveldhuijsen.roosters.dto.UserDataSlim;
import nl.faanveldhuijsen.roosters.dto.mapper.IUserMapper;
import nl.faanveldhuijsen.roosters.model.User;
import nl.faanveldhuijsen.roosters.repository.IUserRepository;
import nl.faanveldhuijsen.roosters.utils.exceptions.ResourceNotFound;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public UserDataSlim update(Long id, UserData data) {
        User user = repo.findById(id).orElse(null);

        if (user != null) {
            user.setName(data.getName());
            user.setEmail(data.getEmail());
            user.setRole(data.getRole());

            return mapper.entityToDataSlim(repo.save(user));
        }

        notFound();
        return null;
    }

    public UserData get(String email) {
        Optional<User> query = repo.findUserByEmail(email);

        return get(query);
    }

    @Override
    public UserData get(Long id) {
        Optional<User> query = repo.findById(id);

        return get(query);
    }

    private UserData get(Optional<User> query) {
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

    /**
     * Only return user if it's the authenticated user
     *
     * @param userId
     * @return
     * @throws ResourceNotFound
     */
    public UserData getAuthenticatedUser(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserData user = get(userId);

        if (auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            if (isAdmin(auth) || userDetails.getUsername().equals(user.getEmail())) {
                return user;
            }
        }
        notFound();
        return null;
    }

    public UserData getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            UserData user = get(userDetails.getUsername());
                return user;
        }
        notFound();
        return null;
    }

    public boolean isAdmin(Authentication auth) {
        for (GrantedAuthority role : auth.getAuthorities()) {
            if (role.getAuthority().equals("ADMIN")) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return isAdmin(auth);
    }
}
