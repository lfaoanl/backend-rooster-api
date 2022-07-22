package nl.faanveldhuijsen.roosters.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc
class DayOffControllerTest extends BaseControllerTest {

    private int dayOffId;
    private String reason;

    @Test
    public void crudDayOff() throws Exception {
        // getDaysOff
        mvc.perform(getAsUser("/daysoff"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        // createDayOff
        this.reason = "Reason " + randomString(8);
        String json = "{" +
                "\"reason\": \"" + reason + "\"," +
                "\"startTime\": \"2022-05-05T00:00:00\"," +
                "\"endTime\": \"2022-05-05T23:59:59\"}";
        MvcResult result = mvc.perform(postAsUser("/daysoff", json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        Map<String, Object> results = readJson(result);
        this.dayOffId = (int) results.get("id");

        assertEquals(this.reason, results.get("reason"));
        System.out.println(dayOffId);

        // showDayOff
        mvc.perform(getAsAdmin("/daysoff/2022/05/05"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().string(containsString(reason)))
                .andReturn();

        // deleteDayoff
        mvc.perform(deleteAsUser("/daysoff/" + dayOffId))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().string(containsString(reason)))
                .andReturn();

    }

}