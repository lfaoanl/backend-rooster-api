package nl.faanveldhuijsen.roosters.service;

import nl.faanveldhuijsen.roosters.dto.ScheduleDataCsv;
import nl.faanveldhuijsen.roosters.service.DateTimeService.DateRange;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    @Autowired
    private DateTimeService dateTimeService;

    private static final String TYPE = "text/csv";

    public boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }


    public List<ScheduleDataCsv> parseCsv(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withDelimiter(';'));

            List<ScheduleDataCsv> scheduleList = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                ScheduleDataCsv scheduleData = new ScheduleDataCsv();

                scheduleData.setEmail(csvRecord.get("email"));
                scheduleData.setTaskName(csvRecord.get("task"));

                DateRange time = dateTimeService.rangeFromString(csvRecord.get("startTime"), csvRecord.get("endTime"));
                scheduleData.setStartTime(time.start);
                scheduleData.setEndTime(time.end);

                scheduleList.add(scheduleData);
            }
            return scheduleList;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV: " + e.getMessage());
        }
    }
}
