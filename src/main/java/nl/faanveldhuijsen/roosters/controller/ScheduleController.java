package nl.faanveldhuijsen.roosters.controller;

import lombok.RequiredArgsConstructor;
import nl.faanveldhuijsen.roosters.dto.ScheduleData;
import nl.faanveldhuijsen.roosters.dto.TaskData;
import nl.faanveldhuijsen.roosters.dto.mapper.IScheduleMapper;
import nl.faanveldhuijsen.roosters.service.DateTimeService;
import nl.faanveldhuijsen.roosters.service.ScheduleService;
import nl.faanveldhuijsen.roosters.utils.DefaultResponse;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;

@RequiredArgsConstructor
@RestController
public class ScheduleController {

    public final ScheduleService schedule;

    public final DefaultResponse response;

    public final IScheduleMapper mapper;

    public final DateTimeService dateTimeService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/schedules")
    public ResponseEntity<Object> showSchedules() {
        return response.ok(schedule.fetch());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/schedules")
    public ResponseEntity<Object> createSchedule(@RequestBody @Valid ScheduleData scheduleData, BindingResult result) {
        if (result.hasErrors()) {
            return response.fieldErrors(result);
        }
        return response.created(schedule.create(scheduleData));
    }

    @GetMapping("/schedules/{id}")
    public ResponseEntity<Object> showSchedule(@PathVariable("id") Long id) {

        return response.ok(schedule.get(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/schedules/{id}")
    public ResponseEntity<Object> updateSchedule(@PathVariable("id") Long id, @Valid @RequestBody ScheduleData data, BindingResult result) {
        if (result.hasErrors()) {
            return response.fieldErrors(result);
        }
        return response.ok(schedule.update(id, data));
    }

    @GetMapping("/schedules/year/{year}/week/{week}")
    public ResponseEntity<Object> showWeek(@PathVariable("year") int year, @PathVariable("week") int week) {

        DateTimeService.DateRange dateRange = dateTimeService.rangeFromWeek(year, week);

        return response.ok(schedule.inBetweenDates(dateRange.startDate, dateRange.endDate));
    }

    @GetMapping("/schedules/{year}/{month}/{day}")
    public ResponseEntity<Object> showDay(@PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day) {

        DateTimeService.DateRange dateRange = dateTimeService.rangeFromDay(year, month, day);
        return response.ok(schedule.inBetweenDates(dateRange.startDate, dateRange.endDate));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/schedules/{id}/delete")
    public ResponseEntity<Object> deleteSchedule(@PathVariable("id") Long id) {
        ScheduleData task = this.schedule.delete(id);

        if (task == null) {
            return response.notFound("Schedule not found");
        }

        return response.ok(task);
    }
}
