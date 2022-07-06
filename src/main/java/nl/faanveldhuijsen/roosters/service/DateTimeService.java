package nl.faanveldhuijsen.roosters.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@Service
public class DateTimeService {

    @RequiredArgsConstructor
    public static class DateRange {
        public final LocalDateTime start;
        public final LocalDateTime end;
    }

    public DateRange rangeFromWeek(int year, int week) {
        // Get first day of the week
        LocalDateTime startDate = LocalDateTime.of(year, Month.JUNE, 1, 0, 0, 0)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week);

        // Add 7 days to go to the start of next monday,
        // minus 1 nano second makes it exactly the end of the week
        LocalDateTime endDate = startDate.plusDays(7).minusNanos(1);

        return new DateRange(startDate, endDate);
    }

    public DateRange rangeFromDay(int year, int month, int day) {
        LocalDateTime startDate = LocalDateTime.of(year, month, day, 0, 0, 0);
        // minus 1 nano second makes it exactly the end of the day
        LocalDateTime endDate = startDate.plusHours(24).minusNanos(1);

        return new DateRange(startDate, endDate);
    }

    public DateRange rangeFromString(String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return new DateRange(LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter));
    }

}
