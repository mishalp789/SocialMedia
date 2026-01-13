package com.mishal.socialmedia.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deleteUser_asUser_shouldReturn403() throws Exception {
        mockMvc.perform(
                delete("/users/test")
                        .with(user("mishal").roles("USER"))
        ).andExpect(status().isForbidden());
    }

    @Test
    void deleteUser_asAdmin_shouldReturn204() throws Exception {

        // 1️⃣ Create a user first
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "username": "test",
                  "email": "test@test.com",
                  "password": "secret123"
                }
            """)
        ).andExpect(status().isCreated());

        // 2️⃣ Delete as admin
        mockMvc.perform(
                delete("/users/test")
                        .with(user("admin").roles("ADMIN"))
        ).andExpect(status().isNoContent());
    }

}
