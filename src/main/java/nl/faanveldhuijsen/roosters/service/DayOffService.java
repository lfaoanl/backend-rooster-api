package nl.faanveldhuijsen.roosters.service;

import lombok.RequiredArgsConstructor;
import nl.faanveldhuijsen.roosters.dto.*;
import nl.faanveldhuijsen.roosters.dto.mapper.IDayOffMapper;
import nl.faanveldhuijsen.roosters.dto.mapper.IUserMapper;
import nl.faanveldhuijsen.roosters.model.DayOff;
import nl.faanveldhuijsen.roosters.model.Schedule;
import nl.faanveldhuijsen.roosters.model.User;
import nl.faanveldhuijsen.roosters.repository.IDayOffRepository;
import nl.faanveldhuijsen.roosters.repository.IUserRepository;
import nl.faanveldhuijsen.roosters.utils.exceptions.ResourceNotFound;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DayOffService implements ICrudService<DayOffData, DayOffData> {

    private final IDayOffRepository repo;
    private final IDayOffMapper mapper;
    private final IUserRepository userRepo;
    private final IUserMapper userMapper;
    public final UserService userService;

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepo.findUserByEmail( ( (UserDetails) auth.getPrincipal() ).getUsername() );

        if (user.isEmpty()) {
            notFound();
            return null;
        }
        return user.get();
    }

    @Override
    public DayOffData create(DayOffData data) {

        UserDataSlim authenticatedUser = userMapper.entityToDataSlim(getAuthenticatedUser());
        data.setUser(authenticatedUser);

        DayOff dayOff = repo.save(mapper.dataToEntity(data));

        repopulateRelationFields(dayOff);

        return this.mapper.entityToData(dayOff);
    }

    @Override
    public DayOffData update(Long id, DayOffData data) {
        Optional<DayOff> query = null;

        if (userService.isAdmin()) {
            query = repo.findById(id);
        } else {
            query = repo.findByIdAndUser(id, getAuthenticatedUser());
        }

        if (query.isPresent()) {
            DayOff dayOff = query.get();
            dayOff.setStartTime(data.getStartTime());
            dayOff.setEndTime(data.getEndTime());
            dayOff.setUser(userMapper.dataToEntity(data.getUser()));

            repopulateRelationFields(dayOff);
            return mapper.entityToData(repo.save(dayOff));
        }

        notFound();
        return null;
    }

    @Override
    public DayOffData get(Long id) {
        Optional<DayOff> query = null;

        if (userService.isAdmin()) {
            query = repo.findById(id);
        } else {
            query = repo.findByIdAndUser(id, getAuthenticatedUser());
        }

        if (query.isEmpty()) {
            notFound();
        }
        return mapper.entityToData(query.get());
    }

    @Override
    public DayOffData delete(Long id) {
        Optional<DayOff> query = null;

        if (userService.isAdmin()) {
            query = repo.findById(id);
        } else {
            query = repo.findByIdAndUser(id, getAuthenticatedUser());
        }

        if (query.isEmpty()) {
            notFound();
        }
        repo.deleteById(id);

        return mapper.entityToData(query.get());
    }

    public void notFound() {
        throw new ResourceNotFound("Day off not found.");
    }

    @Override
    public Collection<DayOffData> fetch() {
        Collection<DayOff> entities = repo.findAllByUser(getAuthenticatedUser());
        ArrayList<DayOffData> daysOff = new ArrayList<>();

        entities.forEach(schedule -> daysOff.add(mapper.entityToData(schedule)));

        return daysOff;
    }

    public Collection<DayOffData> inBetweenDates(LocalDateTime start, LocalDateTime end) {

        List<DayOff> inBetween = null;
        if (userService.isAdmin()) {
            inBetween = this.repo.findAllByStartTimeBetween(start, end);
        } else {
            inBetween = this.repo.findAllByUserAndStartTimeBetween(getAuthenticatedUser(), start, end);
        }

        return this.mapper.entityListToDataList(inBetween);
    }

    private void repopulateRelationFields(DayOff dayOff) {
        // Repopulate relation fields for easy frontend manipulation
        Long userId = dayOff.getUser().getId();
        dayOff.setUser(userRepo.findById(userId).orElse(dayOff.getUser()));
    }
}
