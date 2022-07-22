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
class UserControllerTest extends BaseControllerTest {

    @Test
    void getUsers() throws Exception {
        mvc.perform(getAsAdmin("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(containsString("user@novi.nl")));
    }

    @Test
    void createAndDeleteUser() throws Exception {
        String email = "unit@test.nl";
        String userData = "{\n" +
                "    \"name\": \"Faan\",\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"password\": \"secret\",\n" +
                "    \"role\": \"USER\"\n" +
                "}";

        // Test creation
        MvcResult result = mvc.perform(postAsAdmin("/users", userData))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().string(containsString(email)))
                .andReturn();

        Map<String, Object> map = readJson(result);
        int userId = (int) map.get("id");

        // Test deletion
        mvc.perform(deleteAsAdmin("/users/" + userId))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().string(containsString(email)))
                .andReturn();
    }

    @Test
    void me() throws Exception {
        MvcResult result = mvc.perform(getAsUser("/users/me"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().string(containsString(EMAIL_USER)))
                .andReturn();

        Map<String, Object> map = readJson(result);
        String email = (String) map.get("email");
        assertEquals(email, EMAIL_USER);
    }

    @Test
    void updateMe() throws Exception {
        String name = "Test " + randomString(8);
        String obj = "{\"email\": \""+EMAIL_USER+"\", \"name\": \"" + name + "\"}";
        MvcResult result = mvc.perform(postAsUser("/users/me", obj))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().string(containsString(EMAIL_USER)))
                .andReturn();

        Map<String, Object> map = readJson(result);
        String newName = (String) map.get("name");
        assertEquals(newName, name);
    }

    @Test
    void showUser() throws Exception {
        MvcResult result = mvc.perform(getAsAdmin("/users/2"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().string(containsString(EMAIL_USER)))
                .andReturn();

        Map<String, Object> map = readJson(result);
        String email = (String) map.get("email");
        assertEquals(email, EMAIL_USER);
    }

    @Test
    void updateUser() throws Exception{
        String name = "Test " + randomString(8);
        String obj = "{\"email\": \""+EMAIL_USER+"\", \"name\": \"" + name + "\"}";

        MvcResult result = mvc.perform(postAsAdmin("/users/2", obj))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().string(containsString(EMAIL_USER)))
                .andReturn();

        Map<String, Object> map = readJson(result);
        String newName = (String) map.get("name");
        assertEquals(newName, name);
    }

    @Test
    void getUserSchedules() throws Exception {
        mvc.perform(getAsAdmin("/users/2/schedules"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
    }
}