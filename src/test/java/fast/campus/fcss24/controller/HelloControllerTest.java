package fast.campus.fcss24.controller;

import fast.campus.fcss24.security.WithCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void helloWithoutAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hello"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void helloWithAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hello"))
                .andExpect(content().string("Hello!"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("v2 는 ADMIN 만 접근 가능하다.")
    public void helloWithAuthButInvalidRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/hello"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void helloWithAuthButValidRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/hello"))
                .andExpect(status().isOk());
    }

    @Test
    public void testWithPostProcessor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hello")
                .with(user(User.withUsername("danny.kim")
                        .roles("ADMIN")
                        .password("12345")
                        .build()))
        ).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("danny.kim")
    public void helloAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hello"))
                .andExpect(status().isOk());
    }

    @Test
    @WithCustomUser(username = "danny.kim")
    public void helloAuthenticated2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hello"))
                .andExpect(status().isOk());
    }
}