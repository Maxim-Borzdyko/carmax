package by.bntu.borzdyko.carmax.controller;

import by.bntu.borzdyko.carmax.model.Role;
import by.bntu.borzdyko.carmax.model.User;
import by.bntu.borzdyko.carmax.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerTest {

    private static final User ADMIN = User.builder().id(1L).email("admin@test.com").status(true).role(Role.ADMIN)
            .firstName("first").secondName("second").build();
    private static final User FIRST_USER = User.builder().id(2L).email("user1@test.com").status(true).role(Role.USER)
            .firstName("first").secondName("second").build();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Test
    public void getUsers_tryNotAuthenticatedUser_redirect3xxLogin() throws Exception {
        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }

    @Test
    @WithUserDetails("user1@test.com")
    public void getUsers_tryWithUser_forbidden() throws Exception {
        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void getUsers_tryWithAdmin_redirect3xxLogin() throws Exception {
        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", hasSize(3)))
                .andExpect(model().attribute("users", hasItem(allOf(
                        hasProperty("id", is(FIRST_USER.getId())),
                        hasProperty("email", is(FIRST_USER.getEmail())),
                        hasProperty("firstName", is(FIRST_USER.getFirstName())),
                        hasProperty("secondName", is(FIRST_USER.getSecondName()))
                ))))
                .andExpect(view().name("user/users"));

        assertNotNull(userRepository.findByEmail(FIRST_USER.getEmail()));
    }

    @Test
    public void getProfile_tryNotAuthenticatedUser_redirect3xxLogin() throws Exception {
        this.mockMvc.perform(get("/user/profile"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }

    @Test
    @WithUserDetails("user1@test.com")
    public void getProfile_tryWithUser_profilePage() throws Exception {
        this.mockMvc.perform(get("/user/profile"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", is(FIRST_USER.getEmail())),
                        hasProperty("firstName", is(FIRST_USER.getFirstName())),
                        hasProperty("secondName", is(FIRST_USER.getSecondName()))
                )))
                .andExpect(view().name("profile"));

        assertNotNull(userRepository.findByEmail(FIRST_USER.getEmail()));
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void getProfile_tryWithAdmin_profilePage() throws Exception {
        this.mockMvc.perform(get("/user/profile"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", is(ADMIN.getEmail())),
                        hasProperty("firstName", is(ADMIN.getFirstName())),
                        hasProperty("secondName", is(ADMIN.getSecondName()))
                )))
                .andExpect(view().name("profile"));

        assertNotNull(userRepository.findByEmail(ADMIN.getEmail()));
    }

    @Test
    public void updateUser_tryNotAuthenticatedUser_redirect3xxLogin() throws Exception {
        this.mockMvc.perform(get("/user/update"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }

    @Test
    @WithUserDetails("user1@test.com")
    public void updateUser_tryWithUser_profile() throws Exception {
        String updatedSecondName = "test-updated";

        this.mockMvc.perform(post("/user/update")
                .param("email", FIRST_USER.getEmail()).param("password", "root")
                .param("firstName", FIRST_USER.getFirstName()).param("secondName", updatedSecondName))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"));

        assertEquals(updatedSecondName, userRepository.getOne(FIRST_USER.getId()).getSecondName());
    }

    @Test
    @WithUserDetails("user1@test.com")
    public void updateUser_tryWithWrongInputs_profileWithErrors() throws Exception {
        this.mockMvc.perform(post("/user/update")
                .param("email", "1").param("password", "root")
                .param("firstName", "2").param("secondName", "3"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(model().hasErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));

        assertEquals(FIRST_USER.getEmail(), userRepository.getOne(FIRST_USER.getId()).getEmail());
    }

    @Test
    public void banUser_tryNotAuthenticatedUser_redirect3xxLogin() throws Exception {
        this.mockMvc.perform(post("/user/ban"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }

    @Test
    @WithUserDetails("user1@test.com")
    public void banUser_tryWithUserWithoutPermissions_forbidden() throws Exception {
        this.mockMvc.perform(post("/user/ban"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void banUser_tryWithAdmin_redirect3xxUsers() throws Exception {
        boolean expectedStatus = false;

        this.mockMvc.perform(post("/user/ban")
                .param("id", FIRST_USER.getId().toString()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));

        assertEquals(expectedStatus, userRepository.getOne(FIRST_USER.getId()).getStatus());
    }

    @Test
    public void deleteUser_tryNotAuthenticatedUser_redirect3xxLogin() throws Exception {
        this.mockMvc.perform(delete("/user/2/delete"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }

    @Test
    @WithUserDetails("user1@test.com")
    public void deleteUser_tryWithUserWithoutPermissions_forbidden() throws Exception {
        this.mockMvc.perform(delete("/user/2/delete"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void deleteUser_tryWithAdmin_redirect3xxUsers() throws Exception {
        int expectedSize = userRepository.findAllByRole(Role.USER).size() - 1;

        this.mockMvc.perform(delete("/user/2/delete"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));

        assertEquals(expectedSize, userRepository.findAllByRole(Role.USER).size());
    }
}