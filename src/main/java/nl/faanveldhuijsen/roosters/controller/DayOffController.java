package nl.faanveldhuijsen.roosters.controller;

import lombok.RequiredArgsConstructor;
import nl.faanveldhuijsen.roosters.dto.DayOffData;
import nl.faanveldhuijsen.roosters.dto.DayOffDataSlim;
import nl.faanveldhuijsen.roosters.dto.UserData;
import nl.faanveldhuijsen.roosters.dto.mapper.IUserMapper;
import nl.faanveldhuijsen.roosters.model.User;
import nl.faanveldhuijsen.roosters.service.DateTimeService;
import nl.faanveldhuijsen.roosters.service.DayOffService;
import nl.faanveldhuijsen.roosters.service.UserService;
import nl.faanveldhuijsen.roosters.utils.DefaultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Collection;

@RequiredArgsConstructor
@RestController
public class DayOffController {

    public final DayOffService dayOffService;
    public final DefaultResponse response;
    public final DateTimeService dateTimeService;

    /**
     * @return all daysoff from authenticated user
     */
    @GetMapping("/daysoff")
    public ResponseEntity<Object> fetch() {
        return ResponseEntity.ok(dayOffService.fetch());
    }

    @PostMapping("/daysoff")
    public ResponseEntity<Object> create(@Valid @RequestBody DayOffData dayOffData, BindingResult result) {
        if (result.hasErrors()) {
            return response.fieldErrors(result);
        }

        return ResponseEntity.ok(dayOffService.create(dayOffData));
    }

    /**
     * Lists all days off for given date. Only shows days off for authenticated user if it is not an admin
     * @param year
     * @param month
     * @param day
     * @return
     */
    @GetMapping("/daysoff/{year}/{month}/{day}")
    public ResponseEntity<Object> get(@PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day) {

        DateTimeService.DateRange dateRange = dateTimeService.rangeFromDay(year, month, day);
        Collection<DayOffData> daysOffList = dayOffService.inBetweenDates(dateRange.startDate, dateRange.endDate);

        return ResponseEntity.ok(daysOffList);
    }

    @DeleteMapping("/daysoff/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(dayOffService.delete(id));
    }
}
