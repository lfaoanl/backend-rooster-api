package nl.faanveldhuijsen.roosters.controller;

import lombok.RequiredArgsConstructor;
import nl.faanveldhuijsen.roosters.dto.ScheduleData;
import nl.faanveldhuijsen.roosters.dto.ScheduleDataCsv;
import nl.faanveldhuijsen.roosters.service.CsvService;
import nl.faanveldhuijsen.roosters.service.DateTimeService;
import nl.faanveldhuijsen.roosters.service.DateTimeService.DateRange;
import nl.faanveldhuijsen.roosters.service.ScheduleService;
import nl.faanveldhuijsen.roosters.utils.DefaultResponse;
import nl.faanveldhuijsen.roosters.utils.exceptions.InvalidFileFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ScheduleController {

    public final ScheduleService schedule;

    public final DefaultResponse response;

    public final DateTimeService dateTimeService;

    public final CsvService csvService;

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

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/schedules/upload")
    public ResponseEntity<Object> uploadSchedules(@RequestParam("file") MultipartFile csvSchedules) {

        if (!csvService.hasCSVFormat(csvSchedules)) {
            throw new InvalidFileFormat("Csv file format is required");
        }

        List<ScheduleDataCsv> csvData = csvService.parseCsv(csvSchedules);

        HashMap<String, List<?>> status = schedule.createFromCsv(csvData);

        return response.ok(status);
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

        DateRange dateRange = dateTimeService.rangeFromWeek(year, week);

        return response.ok(schedule.inBetweenDates(dateRange.start, dateRange.end));
    }

    @GetMapping("/schedules/{year}/{month}/{day}")
    public ResponseEntity<Object> showDay(@PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day) {

        DateRange dateRange = dateTimeService.rangeFromDay(year, month, day);
        return response.ok(schedule.inBetweenDates(dateRange.start, dateRange.end));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Object> deleteSchedule(@PathVariable("id") Long id) {
        ScheduleData task = this.schedule.delete(id);

        if (task == null) {
            return response.notFound("Schedule not found");
        }

        return response.ok(task);
    }

}
