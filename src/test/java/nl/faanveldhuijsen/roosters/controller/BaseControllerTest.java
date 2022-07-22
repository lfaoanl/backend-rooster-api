package nl.faanveldhuijsen.roosters.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.faanveldhuijsen.roosters.dto.TokenData;
import nl.faanveldhuijsen.roosters.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

abstract public class BaseControllerTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    private JwtService jwtService;
    protected final String EMAIL_USER = "user@novi.nl";
    protected final String EMAIL_ADMIN = "admin@novi.nl";

    protected HttpHeaders getAuthHeaders(String username) {

        TokenData data = jwtService.generateToken(username);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + data.getToken());

        return headers;
    }

    private HttpHeaders getAuthHeaders() {
        return getAuthHeaders(EMAIL_USER);
    }

    private HttpHeaders getAdminHeaders() {
        return getAuthHeaders(EMAIL_ADMIN);
    }

    protected MockHttpServletRequestBuilder get(String url) {
        return MockMvcRequestBuilders.get(url);
    }

    protected MockHttpServletRequestBuilder getAsUser(String url) {
        return get(url).headers(getAuthHeaders());
    }

    protected MockHttpServletRequestBuilder getAsAdmin(String url) {
        return get(url).headers(getAdminHeaders());
    }

    protected MockHttpServletRequestBuilder postAsUser(String url, String obj) {
        return post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj)
                .headers(getAuthHeaders());
    }

    protected MockHttpServletRequestBuilder postAsAdmin(String url, String obj) {
        return post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj)
                .headers(getAdminHeaders());
    }

    protected MockHttpServletRequestBuilder deleteAsAdmin(String url) {
        return delete(url)
                .headers(getAdminHeaders());
    }

    protected MockHttpServletRequestBuilder deleteAsUser(String url) {
        return delete(url)
                .headers(getAuthHeaders());
    }

    protected Map<String, Object> readJson(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Map.class);
    }

    protected Map<String, Object> readJson(MvcResult result) throws Exception {
        return readJson(result.getResponse().getContentAsString());
    }

    protected String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder creation = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int rand = (int) Math.floor(Math.random() * chars.length());
            creation.append(chars.charAt(rand));
        }
        return creation.toString();
    }

}
