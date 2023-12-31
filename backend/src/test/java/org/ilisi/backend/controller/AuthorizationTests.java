package org.ilisi.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class AuthorizationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnForbiddenForProtectedEndpoint() throws Exception {
        mockMvc.perform(get("/profiles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void shouldReturnForbiddenForProtectedEndpoint2() throws Exception {
        mockMvc.perform(get("/profiles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    void shouldReturnUnauthorizedForMalformedAccessToken() throws Exception {
        mockMvc.perform(get("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid_token")
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.message").value("Invalid token format or token expired")
                );
    }

    @Test
    void shouldReturnUnauthorizedForInvalidAccessToken() throws Exception {
        mockMvc.perform(get("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNjQwNiwiaWF0IjoxNjIyNDk5MjA2fQ.8Z6Z")
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isUnauthorized(),
                        MockMvcResultMatchers.jsonPath("$.message").value("Unauthorized access to this resource with this token")
                );

    }
}