package com.mishal.socialmedia.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPosts_withoutToken_shouldReturn403() throws Exception {
        mockMvc.perform(get("/users/mishal/posts"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getPosts_withUserRole_shouldReturn200() throws Exception {
        mockMvc.perform(
                get("/users/mishal/posts")
                        .with(user("mishal").roles("USER"))
        ).andExpect(status().isOk());
    }
}
