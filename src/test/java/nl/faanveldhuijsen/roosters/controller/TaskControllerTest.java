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
class TaskControllerTest extends BaseControllerTest {

    private String taskName;
    private int taskId;

    @Test
    void crudTasks() throws Exception {
        // getTasks
        mvc.perform(getAsUser("/tasks"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        // createTasks
        this.taskName = "Task " + randomString(8);
        String json = "{\"name\": \"" + taskName + "\"}";
        MvcResult result = mvc.perform(postAsAdmin("/tasks", json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        Map<String, Object> results = readJson(result);
        this.taskId = (int) results.get("id");
        assertEquals(this.taskName, results.get("name"));
        System.out.println(taskId);

        // showTask
        mvc.perform(getAsAdmin("/tasks/" + taskId))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().string(containsString(taskName)))
                .andReturn();

        // updateTask
        this.taskName = "Task " + randomString(8);
        String json2 = "{\"name\": \"" + taskName + "\"}";

        mvc.perform(postAsAdmin("/tasks/" + taskId, json2))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().string(containsString(taskName)))
                .andReturn();

        // deleteTask
        mvc.perform(deleteAsAdmin("/tasks/" + taskId))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().string(containsString(taskName)))
                .andReturn();
    }
}